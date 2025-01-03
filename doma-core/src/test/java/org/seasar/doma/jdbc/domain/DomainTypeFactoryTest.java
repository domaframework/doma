/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
