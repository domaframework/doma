package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;
import org.seasar.doma.message.Message;

public class EmbeddablePropertyMetaFactory {

  private final Context ctx;

  private final VariableElement fieldElement;

  public EmbeddablePropertyMetaFactory(Context ctx, VariableElement fieldElement) {
    assertNotNull(ctx, fieldElement);
    this.ctx = ctx;
    this.fieldElement = fieldElement;
  }

  public EmbeddablePropertyMeta createEmbeddablePropertyMeta() {
    String name = fieldElement.getSimpleName().toString();
    CtType ctType = resolveCtType();
    ColumnReflection columnReflection = ctx.getReflections().newColumnReflection(fieldElement);
    return new EmbeddablePropertyMeta(fieldElement, name, ctType, columnReflection);
  }

  private CtType resolveCtType() {
    CtTypes ctTypes = ctx.getCtTypes();
    CtType ctType =
        ctTypes.toCtType(
            fieldElement.asType(),
            List.of(
                ctTypes::newOptionalCtType,
                ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType,
                ctTypes::newHolderCtType,
                ctTypes::newBasicCtType,
                ctTypes::newEmbeddableCtType));
    ctType.accept(new CtTypeValidator(), null);
    return ctType;
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4299, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4301, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4295, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
        throw new AptException(
            Message.DOMA4296, fieldElement, new Object[] {ctType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitEmbeddableCtType(EmbeddableCtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4297, fieldElement, new Object[] {ctType.getType()});
    }

    @Override
    public Void visitAnyCtType(AnyCtType ctType, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4298, fieldElement, new Object[] {ctType.getType()});
    }
  }
}
