package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SuppressAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
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

public class SqlTemplateSelectQueryMetaFactory
    extends AbstractSqlTemplateQueryMetaFactory<SqlTemplateSelectQueryMeta> {

  public SqlTemplateSelectQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var queryMeta = createSqlFileSelectQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doAnnotation(queryMeta, queryMeta.getSelectAnnot());
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlTemplate(queryMeta);
    return queryMeta;
  }

  private SqlTemplateSelectQueryMeta createSqlFileSelectQueryMeta() {
    var selectAnnot = ctx.getAnnots().newSelectAnnot(methodElement);
    if (selectAnnot == null) {
      return null;
    }
    var queryMeta = new SqlTemplateSelectQueryMeta(methodElement);
    queryMeta.setSelectAnnot(selectAnnot);
    queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
    return queryMeta;
  }

  @Override
  protected void doParameters(final SqlTemplateSelectQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final var parameterMeta = createParameterMeta(parameter);
      parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }

    if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
      if (queryMeta.getFunctionCtType() == null) {
        throw new AptException(Message.DOMA4247, methodElement);
      }
    } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
      if (queryMeta.getCollectorCtType() == null) {
        throw new AptException(Message.DOMA4266, methodElement);
      }
    } else {
      if (queryMeta.getFunctionCtType() != null) {
        var selectAnnot = queryMeta.getSelectAnnot();
        throw new AptException(
            Message.DOMA4248,
            methodElement,
            selectAnnot.getAnnotationMirror(),
            selectAnnot.getStrategy());
      }
    }
  }

  @Override
  protected void doReturnType(final SqlTemplateSelectQueryMeta queryMeta) {
    final var returnMeta = createReturnMeta();
    queryMeta.setReturnMeta(returnMeta);

    if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
      var functionCtType = queryMeta.getFunctionCtType();
      var returnCtType = functionCtType.getReturnCtType();
      if (returnCtType == null
          || !ctx.getTypes().isSameType(returnMeta.getType(), returnCtType.getType())) {
        throw new AptException(
            Message.DOMA4246, methodElement, new Object[] {returnMeta.getType()});
      }
    } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
      var collectorCtType = queryMeta.getCollectorCtType();
      var returnCtType = collectorCtType.getReturnCtType();
      if (returnCtType == null
          || !ctx.getTypes().isSameType(returnMeta.getType(), returnCtType.getType())) {
        throw new AptException(
            Message.DOMA4265, methodElement, new Object[] {returnMeta.getType()});
      }
    } else {
      returnMeta.getCtType().accept(new ReturnCtTypeVisitor(queryMeta, returnMeta), null);
    }
  }

  private class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryParameterMeta parameterMeta;

    public ParamCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    public Void visitFunctionCtType(FunctionCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getFunctionCtType() != null) {
        throw new AptException(Message.DOMA4249, parameterMeta.getElement());
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
        throw new AptException(Message.DOMA4264, parameterMeta.getElement());
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
        throw new AptException(Message.DOMA4053, parameterMeta.getElement());
      }
      queryMeta.setSelectOptionsCtType(ctType);
      queryMeta.setSelectOptionsParameterName(parameterMeta.getName());
      return null;
    }
  }

  private class ParamFunctionTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryParameterMeta parameterMeta;

    public ParamFunctionTargetCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4244, queryMeta.getMethodElement());
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(), null);
    }

    protected class StreamElementCtTypeVisitor
        extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

      @Override
      protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
        throw new AptException(Message.DOMA4245, queryMeta.getMethodElement());
      }

      @Override
      public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
        return null;
      }

      @Override
      public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
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
              Message.DOMA4250, parameterMeta.getElement(), new Object[] {ctType.getTypeName()});
        }
        queryMeta.setEntityCtType(ctType);
        return null;
      }

      @Override
      public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
        var valid =
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
                      public Boolean visitHolderCtType(HolderCtType ctType, Void p)
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

  private class ParamCollectorTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryParameterMeta parameterMeta;

    public ParamCollectorTargetCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4262, queryMeta.getMethodElement());
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
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
            Message.DOMA4263, parameterMeta.getElement(), new Object[] {ctType.getTypeName()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      var valid =
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
                    public Boolean visitHolderCtType(HolderCtType ctType, Void p)
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

  private class ReturnCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryReturnMeta returnMeta;

    private final SuppressAnnot suppressAnnot;

    public ReturnCtTypeVisitor(SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
      this.suppressAnnot = ctx.getAnnots().newSuppressAnnot(queryMeta.getMethodElement());
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4008, returnMeta.getMethodElement(), new Object[] {returnMeta.getType()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(
            Message.DOMA4154,
            returnMeta.getMethodElement(),
            new Object[] {ctType.getQualifiedName()});
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
      ctType
          .getElementCtType()
          .accept(new ReturnListElementCtTypeVisitor(queryMeta, returnMeta), p);
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      if (!isSuppressed(Message.DOMA4274)) {
        ctx.getNotifier()
            .send(Kind.WARNING, Message.DOMA4274, returnMeta.getMethodElement(), new Object[] {});
      }
      queryMeta.setResultStream(true);
      ctType
          .getElementCtType()
          .accept(new ReturnStreamElementCtTypeVisitor(queryMeta, returnMeta), p);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(new ReturnOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
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

    private boolean isSuppressed(Message message) {
      if (suppressAnnot != null) {
        return suppressAnnot.isSuppressed(message);
      }
      return false;
    }
  }

  private class ReturnListElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryReturnMeta returnMeta;

    public ReturnListElementCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4007, returnMeta.getMethodElement(), new Object[] {type.getTypeName()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
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
            Message.DOMA4155, returnMeta.getMethodElement(), new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(new ReturnListOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
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

  private class ReturnStreamElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryReturnMeta returnMeta;

    public ReturnStreamElementCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4271, returnMeta.getMethodElement(), new Object[] {type.getTypeName()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
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
            Message.DOMA4272, returnMeta.getMethodElement(), new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      ctType
          .getElementCtType()
          .accept(new ReturnStreamOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
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

  private class ReturnOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlTemplateSelectQueryMeta queryMeta;

    private final QueryReturnMeta returnMeta;

    public ReturnOptionalElementCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4235, returnMeta.getMethodElement(), new Object[] {type.getTypeName()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
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
            Message.DOMA4234, returnMeta.getMethodElement(), new Object[] {ctType.getType()});
      }
      queryMeta.setEntityCtType(ctType);
      return null;
    }
  }

  private class ReturnListOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final QueryReturnMeta returnMeta;

    public ReturnListOptionalElementCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.returnMeta = returnMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4267, returnMeta.getMethodElement(), new Object[] {type.getTypeName()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }

  private class ReturnStreamOptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final QueryReturnMeta returnMeta;

    protected ReturnStreamOptionalElementCtTypeVisitor(
        SqlTemplateSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.returnMeta = returnMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4267, returnMeta.getMethodElement(), new Object[] {type.getTypeName()});
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }
}
