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
package org.seasar.doma.it.temp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class OrderDaoTest {

  @Test
  public void test_orderId_1(Config config) {
    OrderDao dao = new OrderDaoImpl(config);
    Order order = dao.findById(1);

    assertEquals("accepted", order.getOrderStatus().getCode());
    assertEquals(2, order.getOrderItems().size());
    OrderItem orderItem1 = order.getOrderItems().get(0);
    OrderItem orderItem2 = order.getOrderItems().get(1);
    assertEquals("ITM0000001", orderItem1.getItemCode());
    assertEquals("ITM0000002", orderItem2.getItemCode());
    Item item1 = orderItem1.getItem();
    Item item2 = orderItem2.getItem();
    assertEquals("Orange juice", item1.getName());
    assertEquals("NotePC", item2.getName());
    assertEquals(1, item1.getCategories().size());
    Category item1Category1 = item1.getCategories().get(0);
    assertEquals("CTG0000001", item1Category1.getCode());
    assertEquals(2, item2.getCategories().size());
    Category item2Category1 = item2.getCategories().get(0);
    assertEquals("CTG0000002", item2Category1.getCode());
    Category item2Category2 = item2.getCategories().get(1);
    assertEquals("CTG0000003", item2Category2.getCode());
    assertEquals(2, order.getOrderCoupons().size());
    OrderCoupon orderCoupon1 = order.getOrderCoupons().get(0);
    assertEquals("CPN0000001", orderCoupon1.getCouponCode());
    OrderCoupon orderCoupon2 = order.getOrderCoupons().get(1);
    assertEquals("CPN0000002", orderCoupon2.getCouponCode());
    Coupon orderCoupon1Coupon = orderCoupon1.getCoupon();
    assertEquals(3000, orderCoupon1Coupon.getPrice());
    Coupon orderCoupon2Coupon = orderCoupon2.getCoupon();
    assertEquals(30000, orderCoupon2Coupon.getPrice());
  }

  @Test
  public void test_orderId_2(Config config) {
    OrderDao dao = new OrderDaoImpl(config);
    Order order = dao.findById(2);

    assertEquals("checking", order.getOrderStatus().getCode());
    assertEquals(2, order.getOrderItems().size());
    OrderItem orderItem1 = order.getOrderItems().get(0);
    OrderItem orderItem2 = order.getOrderItems().get(1);
    assertEquals("ITM0000001", orderItem1.getItemCode());
    assertEquals("ITM0000002", orderItem2.getItemCode());
    Item item1 = orderItem1.getItem();
    Item item2 = orderItem2.getItem();
    assertEquals("Orange juice", item1.getName());
    assertEquals("NotePC", item2.getName());
    assertEquals(1, item1.getCategories().size());
    Category item2Category1 = item2.getCategories().get(0);
    assertEquals("CTG0000002", item2Category1.getCode());
    Category item2Category2 = item2.getCategories().get(1);
    assertEquals("CTG0000003", item2Category2.getCode());
    assertEquals(0, order.getOrderCoupons().size());
  }
}
