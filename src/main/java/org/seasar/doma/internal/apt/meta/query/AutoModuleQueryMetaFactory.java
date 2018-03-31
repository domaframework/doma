package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.internal.apt.meta.parameter.*;
import org.seasar.doma.internal.apt.reflection.ResultSetReflection;
import org.seasar.doma.message.Message;

/** @author taedium */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
    extends AbstractQueryMetaFactory<M> {

  protected AutoModuleQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  protected void doParameters(M queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      QueryParameterMeta parameterMeta = createParameterMeta(parameter);
      queryMeta.addParameterMeta(parameterMeta);

      CallableSqlParameterMeta callableSqlParameterMeta = createParameterMeta(parameterMeta);
      queryMeta.addCallableSqlParameterMeta(callableSqlParameterMeta);

      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }
  }

  protected CallableSqlParameterMeta createParameterMeta(final QueryParameterMeta parameterMeta) {
    ResultSetReflection resultSetReflection =
        ctx.getReflections().newResultSetReflection(parameterMeta.getElement());
    if (resultSetReflection != null) {
      return createResultSetParameterMeta(parameterMeta, resultSetReflection);
    }
    if (parameterMeta.isAnnotated(In.class)) {
      return createInParameterMeta(parameterMeta);
    }
    if (parameterMeta.isAnnotated(Out.class)) {
      return createOutParameterMeta(parameterMeta);
    }
    if (parameterMeta.isAnnotated(InOut.class)) {
      return createInOutParameterMeta(parameterMeta);
    }
    throw new AptException(Message.DOMA4066, parameterMeta.getElement());
  }

  protected CallableSqlParameterMeta createResultSetParameterMeta(
      final QueryParameterMeta parameterMeta, final ResultSetReflection resultSetReflection) {
    IterableCtType iterableCtType =
        parameterMeta.getCtType().accept(new ResultSetCtTypeVisitor(parameterMeta), null);
    return iterableCtType
        .getElementCtType()
        .accept(new ResultSetElementCtTypeVisitor(parameterMeta, resultSetReflection), false);
  }

  protected CallableSqlParameterMeta createInParameterMeta(final QueryParameterMeta parameterMeta) {
    return parameterMeta.getCtType().accept(new InCtTypeVisitor(parameterMeta), false);
  }

  protected CallableSqlParameterMeta createOutParameterMeta(
      final QueryParameterMeta parameterMeta) {
    final ReferenceCtType referenceCtType =
        parameterMeta.getCtType().accept(new OutCtTypeVisitor(parameterMeta), null);
    return referenceCtType
        .getReferentCtType()
        .accept(new OutReferentCtTypeVisitor(parameterMeta, referenceCtType), false);
  }

  protected CallableSqlParameterMeta createInOutParameterMeta(
      final QueryParameterMeta parameterMeta) {
    final ReferenceCtType referenceCtType =
        parameterMeta.getCtType().accept(new InOutCtTypeVisitor(parameterMeta), null);
    return referenceCtType
        .getReferentCtType()
        .accept(new InOutReferentCtTypeVisitor(parameterMeta, referenceCtType), false);
  }

  /** @author nakamura-to */
  protected class ResultSetCtTypeVisitor
      extends SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    protected ResultSetCtTypeVisitor(QueryParameterMeta parameterMeta) {
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected IterableCtType defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4062, parameterMeta.getElement());
    }

    @Override
    public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
        throws RuntimeException {
      if (!ctType.isList()) {
        defaultAction(ctType, p);
      }
      return ctType;
    }
  }

  /** @author nakamura-to */
  protected class ResultSetElementCtTypeVisitor
      extends SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    protected final ResultSetReflection resultSetReflection;

    public ResultSetElementCtTypeVisitor(
        QueryParameterMeta parameterMeta, ResultSetReflection resultSetReflection) {
      this.parameterMeta = parameterMeta;
      this.resultSetReflection = resultSetReflection;
    }

    @Override
    protected CallableSqlParameterMeta defaultAction(CtType ctType, Boolean p)
        throws RuntimeException {
      throw new AptException(
          Message.DOMA4186, parameterMeta.getElement(), new Object[] {ctType.getTypeName()});
    }

    @Override
    public CallableSqlParameterMeta visitEntityCtType(EntityCtType ctType, Boolean p)
        throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(
            Message.DOMA4157, parameterMeta.getElement(), new Object[] {ctType.getTypeName()});
      }
      return new EntityListParameterMeta(
          parameterMeta.getName(), ctType, resultSetReflection.getEnsureResultMappingValue());
    }

    @Override
    public CallableSqlParameterMeta visitMapCtType(MapCtType ctType, Boolean p)
        throws RuntimeException {
      return new MapListParameterMeta(parameterMeta.getName(), ctType);
    }

    @Override
    public CallableSqlParameterMeta visitScalarCtType(ScalarCtType ctType, Boolean p)
        throws RuntimeException {
      return new ScalarListParameterMeta(parameterMeta.getName(), ctType, p);
    }

    @Override
    public CallableSqlParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }

  /** @author nakamura-to */
  protected class InCtTypeVisitor
      extends SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    public InCtTypeVisitor(QueryParameterMeta parameterMeta) {
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected CallableSqlParameterMeta defaultAction(CtType ctType, Boolean p)
        throws RuntimeException {
      throw new AptException(
          Message.DOMA4101, parameterMeta.getElement(), new Object[] {ctType.getType()});
    }

    @Override
    public CallableSqlParameterMeta visitScalarCtType(ScalarCtType ctType, Boolean p)
        throws RuntimeException {
      return new ScalarInParameterMeta(parameterMeta.getName(), ctType, p);
    }

    @Override
    public CallableSqlParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }

  /** @author nakamura-to */
  protected class OutCtTypeVisitor
      extends SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    public OutCtTypeVisitor(QueryParameterMeta parameterMeta) {
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected ReferenceCtType defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4098, parameterMeta.getElement());
    }

    @Override
    public ReferenceCtType visitReferenceCtType(ReferenceCtType ctType, Void p)
        throws RuntimeException {
      return ctType;
    }
  }

  /** @author nakamura-to */
  protected class OutReferentCtTypeVisitor
      extends SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    protected final ReferenceCtType referenceCtType;

    public OutReferentCtTypeVisitor(
        QueryParameterMeta parameterMeta, ReferenceCtType referenceCtType) {
      this.parameterMeta = parameterMeta;
      this.referenceCtType = referenceCtType;
    }

    @Override
    protected CallableSqlParameterMeta defaultAction(CtType type, Boolean p)
        throws RuntimeException {
      throw new AptException(
          Message.DOMA4100,
          parameterMeta.getElement(),
          new Object[] {referenceCtType.getReferentCtType().getType()});
    }

    @Override
    public CallableSqlParameterMeta visitScalarCtType(ScalarCtType ctType, Boolean p)
        throws RuntimeException {
      return new ScalarOutParameterMeta(parameterMeta.getName(), ctType, p);
    }

    @Override
    public CallableSqlParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }

  /** @author nakamura-to */
  protected class InOutCtTypeVisitor
      extends SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    public InOutCtTypeVisitor(QueryParameterMeta parameterMeta) {
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected ReferenceCtType defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4111, parameterMeta.getElement());
    }

    @Override
    public ReferenceCtType visitReferenceCtType(ReferenceCtType ctType, Void p)
        throws RuntimeException {
      return ctType;
    }
  }

  /** @author nakamura-to */
  protected class InOutReferentCtTypeVisitor
      extends SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

    protected final QueryParameterMeta parameterMeta;

    protected final ReferenceCtType referenceCtType;

    public InOutReferentCtTypeVisitor(
        QueryParameterMeta parameterMeta, ReferenceCtType referenceCtType) {
      this.parameterMeta = parameterMeta;
      this.referenceCtType = referenceCtType;
    }

    @Override
    protected CallableSqlParameterMeta defaultAction(CtType type, Boolean p)
        throws RuntimeException {
      throw new AptException(
          Message.DOMA4100,
          parameterMeta.getElement(),
          new Object[] {referenceCtType.getReferentCtType().getType()});
    }

    @Override
    public CallableSqlParameterMeta visitScalarCtType(ScalarCtType ctType, Boolean p)
        throws RuntimeException {
      return new ScalarInOutParameterMeta(parameterMeta.getName(), ctType, p);
    }

    @Override
    public CallableSqlParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }
  }
}
