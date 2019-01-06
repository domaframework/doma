package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.internal.apt.meta.MetaConstants;
import org.seasar.doma.message.Message;

public class QueryParameterMetaFactory {

  private final Context ctx;

  private final VariableElement parameterElement;

  private final ExecutableElement methodElement;

  private final TypeElement daoElement;

  public QueryParameterMetaFactory(
      Context ctx, VariableElement parameterElement, QueryMeta queryMeta) {
    assertNotNull(ctx, parameterElement, queryMeta);
    this.ctx = ctx;
    this.parameterElement = parameterElement;
    this.methodElement = queryMeta.getMethodElement();
    this.daoElement = queryMeta.getDaoElement();
  }

  public QueryParameterMeta createQueryParameterMeta() {
    String name = ctx.getElements().getParameterName(parameterElement);
    if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
      throw new AptException(
          Message.DOMA4025, parameterElement, new Object[] {MetaConstants.RESERVED_NAME_PREFIX});
    }
    TypeMirror type = parameterElement.asType();
    CtType ctType = ctx.getCtTypes().newCtType(type, new CtTypeValidator());
    return new QueryParameterMeta(name, ctType, parameterElement, methodElement, daoElement);
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {

    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitSelectOptionsCtType(SelectOptionsCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitIterableCtType(IterableCtType iterableCtType, Void aVoid) throws AptException {
      if (iterableCtType.isRaw()) {
        throw new AptException(Message.DOMA4159, parameterElement, new Object[] {});
      }
      if (iterableCtType.hasWildcard()) {
        throw new AptException(Message.DOMA4160, parameterElement, new Object[] {});
      }
      iterableCtType.getElementCtType().accept(new IterableElementCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4236, parameterElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4237, parameterElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      optionalCtType.getElementCtType().accept(new OptionalElementCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4208, parameterElement, new Object[] {domainCtType.getQualifiedName()});
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4209, parameterElement, new Object[] {domainCtType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitFunctionCtType(FunctionCtType functionCtType, Void aVoid) throws AptException {
      if (functionCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4240, parameterElement, new Object[] {functionCtType.getQualifiedName()});
      }
      if (functionCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4241, parameterElement, new Object[] {functionCtType.getQualifiedName()});
      }
      functionCtType.getTargetCtType().accept(new FunctionTargetCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitCollectorCtType(CollectorCtType collectorCtType, Void aVoid)
        throws AptException {
      if (collectorCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4258, parameterElement, new Object[] {collectorCtType.getQualifiedName()});
      }
      if (collectorCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4259, parameterElement, new Object[] {collectorCtType.getQualifiedName()});
      }
      collectorCtType.getTargetCtType().accept(new CollectorTargetCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitReferenceCtType(ReferenceCtType referenceCtType, Void aVoid)
        throws AptException {
      if (referenceCtType.isRaw()) {
        throw new AptException(Message.DOMA4108, parameterElement, new Object[] {});
      }
      if (referenceCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4112, parameterElement, new Object[] {referenceCtType.getQualifiedName()});
      }
      referenceCtType.getReferentCtType().accept(new ReferenceReferentCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitBiFunctionCtType(BiFunctionCtType biFunctionCtType, Void aVoid)
        throws AptException {
      if (biFunctionCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4438, parameterElement, new Object[] {biFunctionCtType.getQualifiedName()});
      }
      if (biFunctionCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4439, parameterElement, new Object[] {biFunctionCtType.getQualifiedName()});
      }
      return null;
    }
  }

  private class IterableElementCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4212, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4213, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }

  protected class OptionalElementCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4238, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4239,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
            });
      }
      return null;
    }
  }

  protected class FunctionTargetCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(), p);
      return null;
    }

    protected class StreamElementCtTypeVisitor
        extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

      @Override
      public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
        if (ctType.isRaw()) {
          throw new AptException(
              Message.DOMA4242, parameterElement, new Object[] {ctType.getQualifiedName()});
        }
        if (ctType.hasWildcard() || ctType.hasTypevar()) {
          throw new AptException(
              Message.DOMA4243, parameterElement, new Object[] {ctType.getQualifiedName()});
        }
        return null;
      }
    }
  }

  protected class CollectorTargetCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4260, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4261, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }

  protected class ReferenceReferentCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4218, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4219, parameterElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }
}
