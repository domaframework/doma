package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Column;
import org.seasar.doma.Embeddable;

@Embeddable
public class Address {

    private final String city;

    @Column(name = "STREET", insertable = false)
    private final String street;

    public Address(String city, String street) {
        super();
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
