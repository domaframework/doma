package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

@Entity
public class PropertyNameReservedEntity {

  String __name;

  public String get__name() {
    return __name;
  }

  public void set__name(String name) {
    __name = name;
  }
}
