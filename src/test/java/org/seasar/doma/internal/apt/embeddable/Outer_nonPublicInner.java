package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;

public class Outer_nonPublicInner {

  @Embeddable
  static class Inner {

    private final String city;

    private final String street;

    public Inner(String city, String street) {
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
