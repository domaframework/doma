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

import java.util.Objects;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class SqlFileModifyQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileModifyQueryMeta> {

  private final ModifyAnnot modifyAnnot;
  private final QueryKind queryKind;
  private final SqlAnnot sqlAnnot;

  public SqlFileModifyQueryMetaFactory(
      RoundContext ctx,
      TypeElement daoElement,
      ExecutableElement methodElement,
      ModifyAnnot modifyAnnot,
      QueryKind queryKind,
      SqlAnnot sqlAnnot) {
    super(ctx, daoElement, methodElement);
    this.modifyAnnot = Objects.requireNonNull(modifyAnnot);
    this.queryKind = Objects.requireNonNull(queryKind);
    this.sqlAnnot = sqlAnnot; // nullable
  }

  @Override
  public QueryMeta createQueryMeta(AnnotationMirror annotation) {
    SqlFileModifyQueryMeta queryMeta = createSqlFileModifyQueryMeta(annotation);
    doAnnotation(queryMeta);
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta, false, queryMeta.isPopulatable());
    return queryMeta;
  }

  private SqlFileModifyQueryMeta createSqlFileModifyQueryMeta(AnnotationMirror annotation) {
    var queryMeta = new SqlFileModifyQueryMeta(daoElement, methodElement);
    queryMeta.setModifyAnnot(modifyAnnot);
    queryMeta.setQueryKind(queryKind);
    queryMeta.setSqlAnnot(sqlAnnot);
    return queryMeta;
  }

  private void doAnnotation(SqlFileModifyQueryMeta queryMeta) {
    var modifyAnnot = queryMeta.getModifyAnnot();
    var returningAnnot = modifyAnnot.getReturningAnnot();
    if (returningAnnot != null) {
      if (modifyAnnot.getSqlFileValue()) {
        throw new AptException(
            Message.DOMA4491, methodElement, returningAnnot.getAnnotationMirror(), new Object[] {});
      }
      if (queryMeta.getSqlAnnot() != null) {
        throw new AptException(
            Message.DOMA4492, methodElement, returningAnnot.getAnnotationMirror(), new Object[] {});
      }
    }
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
