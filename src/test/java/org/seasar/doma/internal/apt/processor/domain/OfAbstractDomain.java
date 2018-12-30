package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class, factoryMethod = "of")
public abstract class OfAbstractDomain {

  private final int value;

  private OfAbstractDomain(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static OfAbstractDomain of(int value) {
    return new MyDomain(value);
  }

  static class MyDomain extends OfAbstractDomain {

    public MyDomain(int value) {
      super(value);
    }
  }
}
