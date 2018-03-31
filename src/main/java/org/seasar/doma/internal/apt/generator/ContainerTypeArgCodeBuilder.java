package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.box;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;

class ContainerTypeArgCodeBuilder extends SimpleCtTypeVisitor<String, Boolean, RuntimeException> {

  @Override
  protected String defaultAction(CtType ctType, Boolean p) throws RuntimeException {
    throw new AptIllegalStateException("illegalState: " + ctType.getQualifiedName());
  }

  @Override
  public String visitOptionalCtType(OptionalCtType ctType, Boolean p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, true);
  }

  @Override
  public String visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
      throws RuntimeException {
    return String.format("%1$s", OptionalInt.class.getName());
  }

  @Override
  public String visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
      throws RuntimeException {
    return String.format("%1$s", OptionalLong.class.getName());
  }

  @Override
  public String visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
      throws RuntimeException {
    return String.format("%1$s", OptionalDouble.class.getName());
  }

  @Override
  public String visitBasicCtType(BasicCtType ctType, Boolean p) throws RuntimeException {
    if (p) {
      return String.format("%1$s<%2$s>", Optional.class.getName(), box(ctType.getTypeName()));
    }
    return box(ctType.getTypeName());
  }

  @Override
  public String visitHolderCtType(HolderCtType ctType, Boolean p) throws RuntimeException {
    if (p) {
      return String.format("%1$s<%2$s>", Optional.class.getName(), ctType.getTypeName());
    }
    return ctType.getTypeName();
  }
}
