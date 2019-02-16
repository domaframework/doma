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

class PropertyCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

  private static final Code NULL = new Code(p -> p.print("null"));

  private BasicCtType basicCtType;

  private DomainCtType domainCtType;

  @Override
  protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
    assertNotNull(basicCtType);
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

  Code getWrapperSupplierCode() {
    return new Code(p -> p.print("%1$s", basicCtType.getWrapperSupplierCode()));
  }

  Code getDomainDescCode() {
    return domainCtType == null ? NULL : domainCtType.getDescCode();
  }

  Code getDomainTypeCode() {
    return new Code(p -> p.print("%1$s", domainCtType == null ? "Object" : domainCtType.getType()));
  }
}
