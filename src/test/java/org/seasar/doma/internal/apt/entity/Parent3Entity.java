package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

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
