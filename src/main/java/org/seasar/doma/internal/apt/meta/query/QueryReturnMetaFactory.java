package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.message.Message;

public class QueryReturnMetaFactory {

  private final Context ctx;

  private final ExecutableElement methodElement;

  public QueryReturnMetaFactory(Context ctx, ExecutableElement methodElement) {
    assertNotNull(ctx, methodElement);
    this.ctx = ctx;
    this.methodElement = methodElement;
  }

  public QueryReturnMeta createQueryReturnMeta() {
    CtType ctType = createCtType();
    return new QueryReturnMeta(ctx, methodElement, ctType);
  }

  private CtType createCtType() {
    TypeMirror returnType = methodElement.getReturnType();
    CtTypes ctTypes = ctx.getCtTypes();
    CtType ctType =
        ctTypes.toCtType(
            returnType,
            List.of(
                ctTypes::newIterableCtType, ctTypes::newStreamCtType,
                ctTypes::newEntityCtType, ctTypes::newOptionalCtType,
                ctTypes::newOptionalIntCtType, ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newMapCtType));
    ctType.accept(new CtTypeValidator(), null);
    return ctType;
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitIterableCtType(IterableCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(Message.DOMA4109, methodElement, new Object[] {ctType.getType()});
      }
      if (ctType.hasWildcardType()) {
        throw new AptException(Message.DOMA4113, methodElement, new Object[] {ctType.getType()});
      }
      ctType.getElementCtType().accept(new IterableElementValidator(methodElement), null);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4236, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4237, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      ctType.getElementCtType().accept(new OptionalElementValidator(methodElement), null);
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4206, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4207, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }

  private class IterableElementValidator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final ExecutableElement methodElement;

    public IterableElementValidator(ExecutableElement methodElement) {
      this.methodElement = methodElement;
    }

    @Override
    public Void visitHolderCtType(final HolderCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4210, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4211, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }

  private class OptionalElementValidator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final ExecutableElement methodElement;

    public OptionalElementValidator(ExecutableElement methodElement) {
      this.methodElement = methodElement;
    }

    @Override
    public Void visitHolderCtType(final HolderCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4238, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4239, methodElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }
  }
}
