package org.seasar.doma.internal.jdbc.sql;

import example.holder.PhoneNumber;
import java.util.Optional;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.holder.HolderDescFactory;

/** @author nakamura-to */
public class ScalarInParameterTest extends TestCase {

  public void testGetHolderClass() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, PhoneNumber> scalar = holderDesc.createScalar();
    ScalarInParameter<String, PhoneNumber> parameter = new ScalarInParameter<>(() -> scalar);
    Optional<Class<?>> optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  public void testGetHolderClass_optional() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    Scalar<String, Optional<PhoneNumber>> scalar = holderDesc.createOptionalScalar();
    ScalarInParameter<String, Optional<PhoneNumber>> parameter =
        new ScalarInParameter<>(() -> scalar);
    Optional<Class<?>> optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }
}
