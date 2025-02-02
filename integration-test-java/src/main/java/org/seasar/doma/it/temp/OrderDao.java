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

import java.util.function.BiFunction;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface OrderDao {
  @Select(aggregateStrategy = OrderStrategy.class)
  Order findById(int id);
}

@AggregateStrategy(root = Order.class, tableAlias = "o")
interface OrderStrategy {

  @AssociationLinker(propertyPath = "orderStatus", tableAlias = "os")
  BiFunction<Order, OrderStatus, Order> orderStatus =
      (o, os) -> {
        o.setOrderStatus(os);
        return o;
      };

  @AssociationLinker(propertyPath = "orderItems", tableAlias = "oi")
  BiFunction<Order, OrderItem, Order> orderItems =
      (o, oi) -> {
        o.getOrderItems().add(oi);
        return o;
      };

  @AssociationLinker(propertyPath = "orderCoupons", tableAlias = "oc")
  BiFunction<Order, OrderCoupon, Order> orderCoupons =
      (o, oc) -> {
        o.getOrderCoupons().add(oc);
        return o;
      };

  @AssociationLinker(propertyPath = "orderItems.item", tableAlias = "i")
  BiFunction<OrderItem, Item, OrderItem> orderItems$item =
      (oi, i) -> {
        oi.setItem(i);
        return oi;
      };

  @AssociationLinker(propertyPath = "orderCoupons.coupon", tableAlias = "cp")
  BiFunction<OrderCoupon, Coupon, OrderCoupon> orderCoupon$coupon =
      (oc, cp) -> {
        oc.setCoupon(cp);
        return oc;
      };

  @AssociationLinker(propertyPath = "orderItems.item.categories", tableAlias = "ct")
  BiFunction<Item, Category, Item> orderItems$item$categories =
      (i, c) -> {
        i.getCategories().add(c);
        return i;
      };
}
