package org.seasar.doma.internal.jdbc.sql;

import example.domain.PhoneNumber;
import java.util.Optional;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;

/** @author nakamura-to */
public class ScalarInOutParameterTest extends TestCase {

  public void testGetDomainClass() throws Exception {
    DomainType<String, PhoneNumber> domainType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, PhoneNumber> scalar = domainType.createScalar();
    Reference<PhoneNumber> ref = new Reference<>();
    ScalarInOutParameter<String, PhoneNumber> parameter = new ScalarInOutParameter<>(scalar, ref);
    Optional<Class<?>> optional = parameter.getDomainClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  public void testGetDomainClass_optional() throws Exception {
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
