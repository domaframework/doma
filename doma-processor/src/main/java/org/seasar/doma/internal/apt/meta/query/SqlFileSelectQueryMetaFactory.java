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
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.SelectType;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SelectAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.cttype.AggregateStrategyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.message.Message;

public class SqlFileSelectQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlFileSelectQueryMeta> {

  public SqlFileSelectQueryMetaFactory(
      Context ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    SqlFileSelectQueryMeta queryMeta = createSqlFileSelectQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta, queryMeta.isExpandable(), false);
    doAggregateStrategy(queryMeta);
    return queryMeta;
  }

  private SqlFileSelectQueryMeta createSqlFileSelectQueryMeta() {
    SelectAnnot selectAnnot = ctx.getAnnotations().newSelectAnnot(methodElement);
    if (selectAnnot == null) {
      return null;
    }
    SqlFileSelectQueryMeta queryMeta = new SqlFileSelectQueryMeta(daoElement, methodElement);
    queryMeta.setSelectAnnot(selectAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    queryMeta.setSqlAnnot(sqlAnnot);
    return queryMeta;
  }

  @Override
  protected void doParameters(final SqlFileSelectQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
      parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }

    if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
      if (queryMeta.getFunctionCtType() == null) {
        throw new AptException(Message.DOMA4247, methodElement, new Object[] {});
      }
    } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
      if (queryMeta.getCollectorCtType() == null) {
        throw new AptException(Message.DOMA4266, methodElement, new Object[] {});
      }
    } else {
      if (queryMeta.getFunctionCtType() != null) {
        SelectAnnot selectAnnot = queryMeta.getSelectAnnot();
        throw new AptException(
            Message.DOMA4248,
            methodElement,
            selectAnnot.getAnnotationMirror(),
            selectAnnot.getStrategy(),
            new Object[] {});
      }
    }
  }

  @Override
  protected void doReturnType(final SqlFileSelectQueryMeta queryMeta) {
    final QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(returnMeta);

    if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
      FunctionCtType functionCtType = queryMeta.getFunctionCtType();
      CtType returnCtType = functionCtType.getReturnCtType();
      if (!ctx.getMoreTypes().isSameTypeWithErasure(returnMeta.getType(), returnCtType.getType())) {
        throw new AptException(
            Message.DOMA4246,
            methodElement,
            new Object[] {returnMeta.getType(), returnCtType.getType()});
      }
    } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
      CollectorCtType collectorCtType = queryMeta.getCollectorCtType();
      CtType returnCtType = collectorCtType.getReturnCtType();
      if (!ctx.getMoreTypes().isSameTypeWithErasure(returnMeta.getType(), returnCtType.getType())) {
        throw new AptException(
            Message.DOMA4265,
            methodElement,
            new Object[] {returnMeta.getType(), returnCtType.getType()});
      }
    } else {
      returnMeta.getCtType().accept(new ReturnCtTypeVisitor(queryMeta), null);
    }
  }

  private void doAggregateStrategy(SqlFileSelectQueryMeta queryMeta) {
    TypeMirror aggregateStrategyType = queryMeta.getSelectAnnot().getAggregateStrategyValue();
    AggregateStrategyCtType aggregateStrategyCtType =
        ctx.getCtTypes().newAggregateStrategyCtType(aggregateStrategyType);
    if (aggregateStrategyCtType != null) {
      CtType returnCtType = queryMeta.getReturnMeta().getCtType();
      EntityCtType entityCtType = EntityCtType.resolveEntityCtType(returnCtType);
      if (entityCtType == null) {
        throw new AptException(Message.DOMA4473, methodElement, new Object[] {});
      }
      if (queryMeta.getSelectStrategyType() != SelectType.RETURN) {
        throw new AptException(Message.DOMA4484, methodElement, new Object[] {});
      }
      TypeMirror rootType = aggregateStrategyCtType.getAggregateStrategyAnnot().getRootValue();
      EntityCtType rootCtType = ctx.getCtTypes().newEntityCtType(rootType);
      if (rootCtType == null) {
        throw new AptException(Message.DOMA4479, methodElement, new Object[] {});
      }
      if (!entityCtType.isSameType(rootCtType)) {
        throw new AptException(
            Message.DOMA4480,
            methodElement,
            new Object[] {entityCtType.getQualifiedName(), rootCtType.getQualifiedName()});
      }
      queryMeta.setAggregateStrategyCtType(aggregateStrategyCtType);
    }
  }

  static class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamCtTypeVisitor(SqlFileSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    public Void visitFunctionCtType(FunctionCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getFunctionCtType() != null) {
        throw new AptException(Message.DOMA4249, parameterMeta.getElement(), new Object[] {});
      }
      ctType
          .getTargetCtType()
          .accept(new ParamFunctionTargetCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.setFunctionCtType(ctType);
      queryMeta.setFunctionParameterName(parameterMeta.getName());
      return null;
    }

    @Override
    public Void visitCollectorCtType(CollectorCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getCollectorCtType() != null) {
        throw new AptException(Message.DOMA4264, parameterMeta.getElement(), new Object[] {});
      }
      ctType
          .getTargetCtType()
          .accept(new ParamCollectorTargetCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.setCollectorCtType(ctType);
      queryMeta.setCollectorParameterName(parameterMeta.getName());
      return null;
    }

    @Override
    public Void visitSelectOptionsCtType(SelectOptionsCtType ctType, Void p)
        throws RuntimeException {
      if (queryMeta.getSelectOptionsCtType() != null) {
        throw new AptException(Message.DOMA4053, parameterMeta.getElement(), new Object[] {});
      }
      queryMeta.setSelectOptionsCtType(ctType);
      queryMeta.setSelectOptionsParameterName(parameterMeta.getName());
      return null;
    }
  }

  static class ParamFunctionTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamFunctionTargetCtTypeVisitor(
        SqlFileSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4244, queryMeta.getMethodElement(), new Object[] {});
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(), null);
    }

    class StreamElementCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

      @Override
      protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
        throw new AptException(Message.DOMA4245, queryMeta.getMethodElement(), new Object[] {});
      }

      @Override
      public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
        if (ctType.isAbstract()) {
          throw new AptException(
              Message.DOMA4250, parameterMeta.getElement(), new Object[] {ctType.getType()});
        }
        queryMeta.setEntityCtType(ctType);
        return null;
      }

      @Override
      public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
        Boolean valid =
            ctType
                .getElementCtType()
                .accept(
                    new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

                      @Override
                      protected Boolean defaultAction(CtType ctType, Void p)
                          throws RuntimeException {
                        return false;
                      }

                      @Override
                      public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                          throws RuntimeException {
                        return true;
                      }

                      @Override
                      public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                          throws RuntimeException {
                        return true;
                      }
                    },
                    null);
        if (Boolean.FALSE == valid) {
          defaultAction(ctType, null);
        }
        return null;
      }

      @Override
      public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
          throws RuntimeException {
        return null;
      }

      @Override
      public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
          throws RuntimeException {
        return null;
      }
    }
  }

  static class ParamCollectorTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    final QueryParameterMeta parameterMeta;

    ParamCollectorTargetCtTypeVisitor(
        SqlFileSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4262, queryMeta.getMethodElement(), new Object[] {});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(
            Message.DOMA4263, parameterMeta.getElement(), new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      Boolean valid =
          ctType
              .getElementCtType()
              .accept(
                  new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

                    @Override
                    protected Boolean defaultAction(CtType ctType, Void p) throws RuntimeException {
                      return false;
                    }

                    @Override
                    public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                        throws RuntimeException {
                      return true;
                    }

                    @Override
                    public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                        throws RuntimeException {
                      return true;
                    }
                  },
                  null);
      if (Boolean.FALSE == valid) {
        defaultAction(ctType, null);
      }
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return null;
    }
  }

  class ReturnCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    final Suppress suppress;

    ReturnCtTypeVisitor(SqlFileSelectQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
      this.suppress = queryMeta.getMethodElement().getAnnotation(Suppress.class);
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4008, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(
            Message.DOMA4154, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitIterableCtType(IterableCtType ctType, Void p) throws RuntimeException {
      if (!ctType.isList()) {
        defaultAction(ctType, p);
      }
      ctType.getElementCtType().accept(new ReturnListElementCtTypeVisitor(queryMeta), p);
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      if (!isSuppressed()) {
        ctx.getReporter().report(Kind.WARNING, Message.DOMA4274, methodElement, new Object[] {});
      }
      queryMeta.setResultStream(true);
      ctType.getElementCtType().accept(new ReturnStreamElementCtTypeVisitor(queryMeta), p);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType.getElementCtType().accept(new ReturnOptionalElementCtTypeVisitor(queryMeta), p);
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return null;
    }

    boolean isSuppressed() {
      if (suppress != null) {
        for (Message suppressMessage : suppress.messages()) {
          if (suppressMessage == Message.DOMA4274) {
            return true;
          }
        }
      }
      return false;
    }
  }

  class ReturnListElementCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    ReturnListElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4007, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(Message.DOMA4155, methodElement, new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType.getElementCtType().accept(new ReturnListOptionalElementCtTypeVisitor(), p);
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return null;
    }
  }

  class ReturnStreamElementCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    ReturnStreamElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4271, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(Message.DOMA4272, methodElement, new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType.getElementCtType().accept(new ReturnStreamOptionalElementCtTypeVisitor(), p);
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return null;
    }
  }

  class ReturnOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final SqlFileSelectQueryMeta queryMeta;

    ReturnOptionalElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4235, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(Message.DOMA4234, methodElement, new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }
  }

  class ReturnListOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    ReturnListOptionalElementCtTypeVisitor() {}

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4267, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }

  class ReturnStreamOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    ReturnStreamOptionalElementCtTypeVisitor() {}

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4267, methodElement, new Object[] {type.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }
}
