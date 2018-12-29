package org.seasar.doma.jdbc.domain;

import example.domain.PhoneNumber;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class DomainTypeFactoryTest extends TestCase {

  private ClassHelper classHelper = new ClassHelper() {};

  public void testGetDomainType() throws Exception {
    DomainType<String, PhoneNumber> type =
        DomainTypeFactory.getDomainType(PhoneNumber.class, classHelper);
    assertNotNull(type);
  }

  public void testGetDomainType_DomaIllegalArgumentException() throws Exception {
    try {
      DomainTypeFactory.getDomainType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void testGetDomainType_DomainTypeNotFoundException() throws Exception {
    try {
      DomainTypeFactory.getDomainType(Money.class, classHelper);
      fail();
    } catch (DomainTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
