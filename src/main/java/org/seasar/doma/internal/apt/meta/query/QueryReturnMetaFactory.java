package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.message.Message;

class QueryReturnMetaFactory {

  private final Context ctx;

  private final ExecutableElement methodElement;

  private final TypeElement daoElement;

  public QueryReturnMetaFactory(Context ctx, QueryMeta queryMeta) {
    assertNotNull(ctx, queryMeta);
    this.ctx = ctx;
    methodElement = queryMeta.getMethodElement();
    daoElement = queryMeta.getDaoElement();
  }

  public QueryReturnMeta createQueryReturnMeta() {
    TypeMirror type = methodElement.getReturnType();
    CtType ctType = ctx.getCtTypes().newCtType(type, new CtTypeValidator());
    return new QueryReturnMeta(ctType, methodElement, daoElement);
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {
    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4206, methodElement, new Object[] {domainCtType.getQualifiedName()});
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4207, methodElement, new Object[] {domainCtType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4236, methodElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4237, methodElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      optionalCtType.getElementCtType().accept(new OptionalElementCtTypeValidator(), null);
      return null;
    }

    @Override
    public Void visitIterableCtType(IterableCtType iterableCtType, Void aVoid) throws AptException {
      if (iterableCtType.isRaw()) {
        throw new AptException(Message.DOMA4109, methodElement, new Object[] {});
      }
      if (iterableCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4113, methodElement, new Object[] {iterableCtType.getType()});
      }
      iterableCtType.getElementCtType().accept(new IterableElementCtTypeValidator(), null);
      return null;
    }
  }

  private class OptionalElementCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, AptException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws AptException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4238, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4239, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }

  private class IterableElementCtTypeValidator
      extends SimpleCtTypeVisitor<Void, Void, AptException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws AptException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4210, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4211, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }
}
