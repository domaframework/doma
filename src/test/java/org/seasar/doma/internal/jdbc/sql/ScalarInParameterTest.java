package org.seasar.doma.internal.jdbc.sql;

import example.holder.PhoneNumber;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.holder.HolderDescFactory;

public class ScalarInParameterTest extends TestCase {

  public void testGetHolderClass() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    var scalar = holderDesc.createScalar();
    var parameter = new ScalarInParameter<>(() -> scalar);
    var optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }

  public void testGetHolderClass_optional() throws Exception {
    HolderDesc<String, PhoneNumber> holderDesc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, new ClassHelper() {});
    var scalar = holderDesc.createOptionalScalar();
    var parameter = new ScalarInParameter<>(() -> scalar);
    var optional = parameter.getHolderClass();
    assertEquals(PhoneNumber.class, optional.get());
  }
}
