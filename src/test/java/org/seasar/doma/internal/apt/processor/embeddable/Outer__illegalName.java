package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Embeddable;

public class Outer__illegalName {

  @Embeddable
  public static class Inner {

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
