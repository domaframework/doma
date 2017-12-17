package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity(immutable = true)
public class ImmutableUser {

    @Id
    final private Integer id;

    final private UserAddress address;

    public ImmutableUser(Integer id, UserAddress address) {
        this.id = id;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public UserAddress getAddress() {
        return address;
    }

}
