package org.seasar.doma.it.temp;

import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_order")
public class Order implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  private int id;
  @Association private OrderStatus orderStatus;
  @Association List<OrderItem> orderItems = new ArrayList<>();
  @Association List<OrderCoupon> orderCoupons = new ArrayList<>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public List<OrderCoupon> getOrderCoupons() {
    return orderCoupons;
  }

  public void setOrderCoupons(List<OrderCoupon> orderCoupons) {
    this.orderCoupons = orderCoupons;
  }
}
