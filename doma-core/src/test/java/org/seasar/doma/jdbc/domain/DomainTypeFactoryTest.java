package org.seasar.doma.jdbc.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import example.domain.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

public class DomainTypeFactoryTest {

  private final ClassHelper classHelper = new ClassHelper() {};

  @Test
  public void testGetDomainType() {
    DomainType<String, PhoneNumber> type =
        DomainTypeFactory.getDomainType(PhoneNumber.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetDomainType_DomaIllegalArgumentException() {
    try {
      DomainTypeFactory.getDomainType(Object.class, classHelper);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetDomainType_DomainTypeNotFoundException() {
    try {
      DomainTypeFactory.getDomainType(Money.class, classHelper);
      fail();
    } catch (DomainTypeNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetExternalDomainType() {
    DomainType<String, Job> type = DomainTypeFactory.getExternalDomainType(Job.class, classHelper);
    assertNotNull(type);
  }

  @Test
  public void testGetExternalDomainType_array() {
    DomainType<Object, String[]> type =
        DomainTypeFactory.getExternalDomainType(String[].class, classHelper);
    assertNotNull(type);
  }
}
