package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class User {

  @Id public Integer id;

  public UserAddress address;
}
