package org.seasar.doma.internal.jdbc.sql;

import example.domain.PhoneNumber;
import java.util.Optional;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;

public class ScalarInParameterTest extends TestCase {

  public void testGetDomainClass() throws Exception {
    DomainType<String, PhoneNumber> domainType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, PhoneNumber> scalar = domainType.createScalar();
    ScalarInParameter<String, PhoneNumber> parameter = new ScalarInParameter<>(scalar);
    Optional<Class<?>> optional = parameter.getDomainClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  public void testGetDomainClass_optional() throws Exception {
    DomainType<String, PhoneNumber> domainType =
        DomainTypeFactory.getDomainType(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, Optional<PhoneNumber>> scalar = domainType.createOptionalScalar();
    ScalarInParameter<String, Optional<PhoneNumber>> parameter = new ScalarInParameter<>(scalar);
    Optional<Class<?>> optional = parameter.getDomainClass();
    assertEquals(PhoneNumber.class, optional.get());
  }
}
