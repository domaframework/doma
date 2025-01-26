package org.seasar.doma.it.temp;

import org.seasar.doma.AssociationLinker;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import java.util.function.BiFunction;

@Dao
public interface OrderDao {
    @Select(aggregateHelper = OrderHelper.class)
    Order findById(int id);
}

interface OrderHelper {

    @AssociationLinker(propertyPath = "orderStatus", columnPrefix = "os_")
    BiFunction<Order, OrderStatus, Order> orderStatus = (o,os)-> {
        o.setOrderStatus(os);
        return o;
    };

    @AssociationLinker(propertyPath = "orderItems", columnPrefix = "oi_")
    BiFunction<Order, OrderItem, Order> orderItems = (o,oi)-> {
        o.getOrderItems().add(oi);
        return o;
    };

    @AssociationLinker(propertyPath = "orderCoupons", columnPrefix = "oc_")
    BiFunction<Order, OrderCoupon, Order> orderCoupons = (o,oc)-> {
        o.getOrderCoupons().add(oc);
        return o;
    };

    @AssociationLinker(propertyPath = "orderItems.item", columnPrefix = "i_")
    BiFunction<OrderItem, Item, OrderItem> orderItems$item = (oi,i)-> {
        oi.setItem(i);
        return oi;
    };


    @AssociationLinker(propertyPath = "orderCoupons.coupon", columnPrefix = "cp_")
    BiFunction<OrderCoupon, Coupon, OrderCoupon> orderCoupon$coupon = (oc,cp)-> {
        oc.setCoupon(cp);
        return oc;
    };

    @AssociationLinker(propertyPath = "orderItems.item.categories", columnPrefix = "ct_")
    BiFunction<Item, Category, Item> orderItems$item$categories = (i,c)-> {
        i.getCategories().add(c);
        return i;
    };
}
