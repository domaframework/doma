package org.seasar.doma.internal.apt.generator;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.util.Pair;

public class UnwrapOptionalVisitor
    extends SimpleCtTypeVisitor<Pair<CtType, TypeMirror>, Void, RuntimeException> {

  @Override
  protected Pair<CtType, TypeMirror> defaultAction(CtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitBasicCtType(BasicCtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getBoxedType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitDomainCtType(DomainCtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalCtType(OptionalCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }
}
