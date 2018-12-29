package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;

public class NotTopLevel {

  @Embeddable
  public static class Address {

    private final String city;

    private final String street;

    public Address(String city, String street) {
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
}
