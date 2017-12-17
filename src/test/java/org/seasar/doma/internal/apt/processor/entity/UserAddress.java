package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Embeddable;

@Embeddable
public class UserAddress {

    private final String city;

    private final String street;

    public UserAddress(String city, String street) {
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

}
