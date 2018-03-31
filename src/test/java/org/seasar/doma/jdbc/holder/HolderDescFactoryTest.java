package org.seasar.doma.jdbc.holder;

import example.holder.PhoneNumber;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class HolderDescFactoryTest extends TestCase {

  private ClassHelper classHelper = new ClassHelper() {};

  public void testGetHolderDesc() throws Exception {
    HolderDesc<String, PhoneNumber> desc =
        HolderDescFactory.getHolderDesc(PhoneNumber.class, classHelper);
    assertNotNull(desc);
  }

  public void testGetHolderDesc_DomaIllegalArgumentException() throws Exception {
    try {
      HolderDescFactory.getHolderDesc(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void testGetHolderType_HolderTypeNotFoundException() throws Exception {
    try {
      HolderDescFactory.getHolderDesc(Money.class, classHelper);
      fail();
    } catch (HolderDescNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
