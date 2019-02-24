package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.generator.ScalarMetaFactory.ScalarMeta;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

class ScalarMetaFactory extends SimpleCtTypeVisitor<ScalarMeta, Boolean, RuntimeException> {

  private BasicCtType basicCtType;

  private Code containerTypeCode;

  private Code supplierCode;

  @Override
  protected ScalarMeta defaultAction(CtType ctType, Boolean optional) {
    assertNotNull(basicCtType, containerTypeCode, supplierCode);
    return new ScalarMeta();
  }

  @Override
  public ScalarMeta visitOptionalCtType(OptionalCtType ctType, Boolean optional) {
    return ctType.getElementCtType().accept(this, true);
  }

  @Override
  public ScalarMeta visitOptionalIntCtType(OptionalIntCtType ctType, Boolean optional) {
    basicCtType = ctType.getElementCtType();
    containerTypeCode = new Code(p -> p.print("%1$s", OptionalInt.class));
    supplierCode = new Code(p -> p.print("%1$s::new", OptionalIntScalar.class));
    return defaultAction(ctType, optional);
  }

  @Override
  public ScalarMeta visitOptionalLongCtType(OptionalLongCtType ctType, Boolean optional) {
    basicCtType = ctType.getElementCtType();
    containerTypeCode = new Code(p -> p.print("%1$s", OptionalLong.class));
    supplierCode = new Code(p -> p.print("%1$s::new", OptionalLongScalar.class));
    return defaultAction(ctType, optional);
  }

  @Override
  public ScalarMeta visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean optional) {
    basicCtType = ctType.getElementCtType();
    containerTypeCode = new Code(p -> p.print("%1$s", OptionalDouble.class));
    supplierCode = new Code(p -> p.print("%1$s::new", OptionalDoubleScalar.class));
    return defaultAction(ctType, optional);
  }

  @Override
  public ScalarMeta visitBasicCtType(BasicCtType basicCtType, Boolean optional) {
    this.basicCtType = basicCtType;
    containerTypeCode =
        new Code(
            p -> {
              if (optional) {
                p.print("%1$s<%2$s>", Optional.class, basicCtType.getBoxedType());
              } else {
                p.print("%1$s", basicCtType.getBoxedType());
              }
            });
    supplierCode =
        new Code(
            p -> {
              if (optional) {
                p.print(
                    "() -> new %1$s<>(%2$s)",
                    OptionalBasicScalar.class, basicCtType.getWrapperSupplierCode());
              } else {
                p.print(
                    "() -> new %1$s<>(%2$s)",
                    BasicScalar.class, basicCtType.getWrapperSupplierCode());
              }
            });
    return defaultAction(basicCtType, optional);
  }

  @Override
  public ScalarMeta visitDomainCtType(DomainCtType domainCtType, Boolean optional) {
    basicCtType = domainCtType.getBasicCtType();
    containerTypeCode =
        new Code(
            p -> {
              if (optional) {
                p.print("%1$s<%2$s>", Optional.class, domainCtType.getType());
              } else {
                p.print("%1$s", domainCtType.getType());
              }
            });
    supplierCode =
        new Code(
            p -> {
              if (optional) {
                p.print("%1$s::createOptionalScalar", domainCtType.getDescCode());
              } else {
                p.print("%1$s::createScalar", domainCtType.getDescCode());
              }
            });
    return defaultAction(domainCtType, optional);
  }

  public class ScalarMeta {

    public TypeMirror getBasicType() {
      return basicCtType.getBoxedType();
    }

    public Code getContainerType() {
      return containerTypeCode;
    }

    public Code getSupplier() {
      return supplierCode;
    }
  }
}
