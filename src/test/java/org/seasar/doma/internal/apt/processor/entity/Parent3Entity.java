package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

/**
 * @author taedium
 * 
 */
@Entity(listener = Parent3EntityListener.class)
public class Parent3Entity {

    Integer aaa;

    Integer bbb;

    public Integer getAaa() {
        return aaa;
    }

    public void setAaa(Integer aaa) {
        this.aaa = aaa;
    }

}
