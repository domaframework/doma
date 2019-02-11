package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;

class PropertyCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

  private static final String NULL = "null";

  private BasicCtType basicCtType;

  private WrapperCtType wrapperCtType;

  private DomainCtType domainCtType;

  @Override
  protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
    assertNotNull(basicCtType);
    assertNotNull(wrapperCtType);
    return null;
  }

  @Override
  public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, p);
  }

  @Override
  public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, p);
  }

  @Override
  public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
    return ctType.getElementCtType().accept(this, p);
  }

  @Override
  public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, p);
  }

  @Override
  public Void visitBasicCtType(BasicCtType basicCtType, Void p) throws RuntimeException {
    this.basicCtType = basicCtType;
    this.wrapperCtType = basicCtType.getWrapperCtType();
    return defaultAction(basicCtType, p);
  }

  @Override
  public Void visitDomainCtType(DomainCtType domainCtType, Void p) throws RuntimeException {
    this.domainCtType = domainCtType;
    return visitBasicCtType(domainCtType.getBasicCtType(), p);
  }

  BasicCtType getBasicCtType() {
    return basicCtType;
  }

  LazyFormatter getWrapperCode() {
    return new LazyFormatter(
        c -> {
          if (basicCtType.isEnum()) {
            return String.format(
                "new %s(%s.class)",
                c.apply(wrapperCtType.getType()), c.apply(basicCtType.getBoxedType()));
          }
          return String.format("new %s()", c.apply(wrapperCtType.getType()));
        });
  }

  LazyFormatter getDomainDescCode() {
    return new LazyFormatter(
        __ -> domainCtType == null ? NULL : domainCtType.domainDescSingletonCode());
  }

  LazyFormatter getDomainTypeCode() {
    return new LazyFormatter(
        c -> domainCtType == null ? "Object" : c.apply(domainCtType.getType()));
  }
}
