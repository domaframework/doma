package org.seasar.doma.internal.apt.processor.domain;

import java.util.Arrays;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
public interface InterfaceDomain {

  String getValue();

  static InterfaceDomain of(String value) {
    return Arrays.<InterfaceDomain>stream(DefinedColor.values())
        .filter(a -> a.getValue().equals(value))
        .findFirst()
        .orElseGet(() -> new Color(value));
  }

  enum DefinedColor implements InterfaceDomain {
    RED,
    BLUE,
    GREEN;

    @Override
    public String getValue() {
      return name();
    }
  }

  class Color implements InterfaceDomain {

    private final String value;

    public Color(String value) {
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }
  }
}
