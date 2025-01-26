package org.seasar.doma.it.temp;

import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.io.Serializable;

@Entity
@Table(name = "t_order_coupon")
public class OrderCoupon implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int orderId;
    @Id
    private String couponCode;
    @Association  private Coupon coupon;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}