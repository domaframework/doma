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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.annot.SqlProcessorAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.ConfigCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.PreparedSqlCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class SqlProcessorQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlProcessorQueryMeta> {

  public SqlProcessorQueryMetaFactory(
      RoundContext ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta(AnnotationMirror annotation) {
    SqlProcessorQueryMeta queryMeta = createSqlContentQueryMeta(annotation);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta, false, false);
    return queryMeta;
  }

  private SqlProcessorQueryMeta createSqlContentQueryMeta(AnnotationMirror annotation) {
    SqlProcessorAnnot sqlProcessorAnnot = ctx.getAnnotations().newSqlProcessorAnnot(annotation);
    if (sqlProcessorAnnot == null) {
      return null;
    }
    SqlProcessorQueryMeta queryMeta = new SqlProcessorQueryMeta(daoElement, methodElement);
    queryMeta.setSqlProcessorAnnot(sqlProcessorAnnot);
    queryMeta.setQueryKind(QueryKind.SQL_PROCESSOR);
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    queryMeta.setSqlAnnot(sqlAnnot);
    return queryMeta;
  }

  @Override
  protected void doParameters(SqlProcessorQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
      parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }

    if (queryMeta.getBiFunctionCtType() == null) {
      SqlProcessorAnnot sqlProcessorAnnot = queryMeta.getSqlProcessorAnnot();
      throw new AptException(
          Message.DOMA4433,
          methodElement,
          sqlProcessorAnnot.getAnnotationMirror(),
          new Object[] {});
    }
  }

  @Override
  protected void doReturnType(SqlProcessorQueryMeta queryMeta) {
    final QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(returnMeta);

    BiFunctionCtType biFunctionCtType = queryMeta.getBiFunctionCtType();
    CtType resultCtType = biFunctionCtType.getResultCtType();
    if (!isConvertibleReturnType(returnMeta, resultCtType)) {
      throw new AptException(
          Message.DOMA4436,
          methodElement,
          new Object[] {returnMeta.getType(), resultCtType.getType()});
    }
  }

  private boolean isConvertibleReturnType(QueryReturnMeta returnMeta, CtType resultCtType) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(returnMeta.getType(), resultCtType.getType())) {
      return true;
    }
    if (returnMeta.getType().getKind() == TypeKind.VOID) {
      return ctx.getMoreTypes().isSameTypeWithErasure(resultCtType.getType(), Void.class);
    }
    return false;
  }

  static class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlProcessorQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamCtTypeVisitor(SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getBiFunctionCtType() != null) {
        throw new AptException(Message.DOMA4434, parameterMeta.getElement(), new Object[] {});
      }
      ctType
          .getFirstArgCtType()
          .accept(new ParamBiFunctionFirstArgCtTypeVisitor(queryMeta, parameterMeta), null);
      ctType
          .getSecondArgCtType()
          .accept(new ParamBiFunctionSecondArgCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.setBiFunctionCtType(ctType);
      queryMeta.setBiFunctionParameterName(parameterMeta.getName());
      return null;
    }
  }

  static class ParamBiFunctionFirstArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlProcessorQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamBiFunctionFirstArgCtTypeVisitor(
        SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4437, queryMeta.getMethodElement(), new Object[] {});
    }

    @Override
    public Void visitConfigCtType(ConfigCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }

  static class ParamBiFunctionSecondArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlProcessorQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamBiFunctionSecondArgCtTypeVisitor(
        SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4435, queryMeta.getMethodElement(), new Object[] {});
    }

    @Override
    public Void visitPreparedSqlCtType(PreparedSqlCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }
}
