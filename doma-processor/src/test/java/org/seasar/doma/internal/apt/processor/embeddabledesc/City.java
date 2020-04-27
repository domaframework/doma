package org.seasar.doma.internal.apt.processor.embeddabledesc;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class City {
  private final String value;

  public City(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
