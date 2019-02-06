package org.seasar.doma.jdbc.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import example.domain.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class DomainTypeFactoryTest {

  private ClassHelper classHelper = new ClassHelper() {};

  @Test
  public void testGetDomainType() throws Exception {
    DomainType<String, PhoneNumber> type =
        DomainTypeFactory.getDomainType(PhoneNumber.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetDomainType_DomaIllegalArgumentException() throws Exception {
    try {
      DomainTypeFactory.getDomainType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetDomainType_DomainTypeNotFoundException() throws Exception {
    try {
      DomainTypeFactory.getDomainType(Money.class, classHelper);
      fail();
    } catch (DomainTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
