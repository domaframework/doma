package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity(immutable = true)
public class ImmutableUser {

  @Id private final Integer id;

  private final int age;

  private final UserAddress address;

  public ImmutableUser(Integer id, int age, UserAddress address) {
    this.id = id;
    this.age = age;
    this.address = address;
  }

  public Integer getId() {
    return id;
  }

  public int getAge() {
    return age;
  }

  public UserAddress getAddress() {
    return address;
  }
}
