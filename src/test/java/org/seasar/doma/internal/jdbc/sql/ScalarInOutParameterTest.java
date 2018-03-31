package org.seasar.doma.internal.jdbc.sql;

import example.holder.PhoneNumber;
import java.util.Optional;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.holder.HolderDescFactory;

public class ScalarInOutParameterTest extends TestCase {

  public void testGetHolderClass() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, PhoneNumber> scalar = holderDesc.createScalar();
    Reference<PhoneNumber> ref = new Reference<>();
    ScalarInOutParameter<String, PhoneNumber> parameter =
        new ScalarInOutParameter<>(() -> scalar, ref);
    Optional<Class<?>> optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  public void testGetHolderClass_optional() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, Optional<PhoneNumber>> scalar = holderDesc.createOptionalScalar();
    Reference<Optional<PhoneNumber>> ref = new Reference<>();
    ScalarInOutParameter<String, Optional<PhoneNumber>> parameter =
        new ScalarInOutParameter<>(() -> scalar, ref);
    Optional<Class<?>> optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }
}
