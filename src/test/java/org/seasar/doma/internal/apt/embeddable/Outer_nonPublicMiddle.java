package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;

public class Outer_nonPublicMiddle {

  static class Middle {

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
}
