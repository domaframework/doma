package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.box;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.*;

class BasicTypeArgCodeBuilder extends SimpleCtTypeVisitor<String, Void, RuntimeException> {

  @Override
  protected String defaultAction(CtType ctType, Void p) throws RuntimeException {
    throw new AptIllegalStateException("illegalState: " + ctType.getQualifiedName());
  }

  @Override
  public String visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public String visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public String visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public String visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public String visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
    return box(ctType.getTypeName());
  }

  @Override
  public String visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
    return visitBasicCtType(ctType.getBasicCtType(), p);
  }
}
