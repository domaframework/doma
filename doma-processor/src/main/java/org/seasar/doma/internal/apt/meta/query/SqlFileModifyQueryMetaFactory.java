/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class SqlFileModifyQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileModifyQueryMeta> {

  public SqlFileModifyQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SqlFileModifyQueryMeta queryMeta = createSqlFileModifyQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta, false, queryMeta.isPopulatable());
    return queryMeta;
  }

  private SqlFileModifyQueryMeta createSqlFileModifyQueryMeta() {
    SqlFileModifyQueryMeta queryMeta = new SqlFileModifyQueryMeta(daoElement, methodElement);
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    queryMeta.setSqlAnnot(sqlAnnot);
    ModifyAnnot modifyAnnot = ctx.getAnnotations().newInsertAnnot(methodElement);
    if (modifyAnnot != null && usesSqlTemplate(sqlAnnot, modifyAnnot)) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_INSERT);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newUpdateAnnot(methodElement);
    if (modifyAnnot != null && usesSqlTemplate(sqlAnnot, modifyAnnot)) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_UPDATE);
      return queryMeta;
    }
    modifyAnnot = ctx.getAnnotations().newDeleteAnnot(methodElement);
    if (modifyAnnot != null && usesSqlTemplate(sqlAnnot, modifyAnnot)) {
      queryMeta.setModifyAnnot(modifyAnnot);
      queryMeta.setQueryKind(QueryKind.SQLFILE_DELETE);
      return queryMeta;
    }
    return null;
  }

  private boolean usesSqlTemplate(SqlAnnot sqlAnnot, ModifyAnnot modifyAnnot) {
    return sqlAnnot != null || modifyAnnot.getSqlFileValue();
  }

  @Override
  protected void doReturnType(SqlFileModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isResult(entityCtType)) {
        throw new AptException(Message.DOMA4222, methodElement, new Object[] {});
      }
    } else {
      if (!returnMeta.isPrimitiveInt()) {
        throw new AptException(Message.DOMA4001, methodElement, new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(final SqlFileModifyQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
      if (queryMeta.getEntityCtType() != null) {
        continue;
      }
      parameterMeta
          .getCtType()
          .accept(
              new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                @Override
                public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
                  queryMeta.setEntityCtType(ctType);
                  queryMeta.setEntityParameterName(parameterMeta.getName());
                  return null;
                }
              },
              null);
    }
  }
}
