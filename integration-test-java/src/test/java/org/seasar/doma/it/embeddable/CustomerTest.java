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
package org.seasar.doma.it.embeddable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.CustomerDao;
import org.seasar.doma.it.dao.CustomerDaoImpl;
import org.seasar.doma.it.entity.Customer;
import org.seasar.doma.it.entity.CustomerAddress;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class CustomerTest {

  @Test
  public void testSelect(Config config) {
    CustomerDao dao = new CustomerDaoImpl(config);

    Customer customer = dao.selectById(1);
    assertEquals(1, customer.getCustomerId());

    CustomerAddress billingAddress = customer.getBillingAddress();
    assertNotNull(billingAddress);
    assertEquals("123 MAIN ST", billingAddress.street());
    assertEquals("TOKYO", billingAddress.city());
    assertEquals("100-0001", billingAddress.zipCode());

    CustomerAddress shippingAddress = customer.getShippingAddress();
    assertNotNull(shippingAddress);
    assertEquals("456 OAK AVE", shippingAddress.street());
    assertEquals("YOKOHAMA", shippingAddress.city());
    assertEquals("220-0012", shippingAddress.zipCode());
  }

  @Test
  public void testInsert(Config config) {
    CustomerDao dao = new CustomerDaoImpl(config);

    Customer customer = new Customer();
    customer.setCustomerId(10);

    CustomerAddress billingAddress = new CustomerAddress("OSAKA", "530-0001", "789 RIVER RD");
    customer.setBillingAddress(billingAddress);

    CustomerAddress shippingAddress = new CustomerAddress("KYOTO", "600-8216", "321 HILL ST");
    customer.setShippingAddress(shippingAddress);

    int result = dao.insert(customer);
    assertEquals(1, result);

    Customer inserted = dao.selectById(10);
    assertNotNull(inserted);
    assertEquals(10, inserted.getCustomerId());

    CustomerAddress insertedBilling = inserted.getBillingAddress();
    assertNotNull(insertedBilling);
    assertEquals("789 RIVER RD", insertedBilling.street());
    assertEquals("OSAKA", insertedBilling.city());
    assertEquals("530-0001", insertedBilling.zipCode());

    CustomerAddress insertedShipping = inserted.getShippingAddress();
    assertNotNull(insertedShipping);
    assertEquals("321 HILL ST", insertedShipping.street());
    assertEquals("KYOTO", insertedShipping.city());
    assertEquals("600-8216", insertedShipping.zipCode());
  }

  @Test
  public void testInsert_emptySippingAddress(Config config) {
    CustomerDao dao = new CustomerDaoImpl(config);

    Customer customer = new Customer();
    customer.setCustomerId(10);

    CustomerAddress billingAddress = new CustomerAddress("OSAKA", "530-0001", "789 RIVER RD");
    customer.setBillingAddress(billingAddress);

    int result = dao.insert(customer);
    assertEquals(1, result);

    Customer inserted = dao.selectById(10);
    assertNotNull(inserted);
    assertEquals(10, inserted.getCustomerId());

    CustomerAddress insertedBilling = inserted.getBillingAddress();
    assertNotNull(insertedBilling);
    assertEquals("789 RIVER RD", insertedBilling.street());
    assertEquals("OSAKA", insertedBilling.city());
    assertEquals("530-0001", insertedBilling.zipCode());

    CustomerAddress insertedShipping = inserted.getShippingAddress();
    assertNotNull(insertedShipping);
    assertNull(insertedShipping.street());
    assertNull(insertedShipping.city());
    assertNull(insertedShipping.zipCode());
  }
}
