package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

/**
 * @author taedium
 * 
 */
@Entity
public class PrivatePropertyEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
