package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel(scopes = {RoomScope.class}))
class Room {
    Long id;
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}