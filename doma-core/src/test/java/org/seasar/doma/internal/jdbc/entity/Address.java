package org.seasar.doma.internal.jdbc.entity;

/** */
public class Address extends Location {

  public final String city;

  public final String street;

  public Address(String kind, String city, String street) {
    this.kind = kind;
    this.city = city;
    this.street = street;
  }
}
