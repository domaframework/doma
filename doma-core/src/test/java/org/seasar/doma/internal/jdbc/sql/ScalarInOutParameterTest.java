package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.domain.PhoneNumber;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ScalarInOutParameterTest {

  @Test
  public void testGetDomainClass() {
    DomainType<String, PhoneNumber> domainType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, PhoneNumber> scalar = domainType.createScalar();
    Reference<PhoneNumber> ref = new Reference<>();
    ScalarInOutParameter<String, PhoneNumber> parameter = new ScalarInOutParameter<>(scalar, ref);
    Optional<Class<?>> optional = parameter.getDomainClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  @Test
  public void testGetDomainClass_optional() {
    DomainType<String, PhoneNumber> domainType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, Optional<PhoneNumber>> scalar = domainType.createOptionalScalar();
    Reference<Optional<PhoneNumber>> ref = new Reference<>();
    ScalarInOutParameter<String, Optional<PhoneNumber>> parameter =
        new ScalarInOutParameter<>(scalar, ref);
    Optional<Class<?>> optional = parameter.getDomainClass();
    assertEquals(PhoneNumber.class, optional.get());
  }
}
