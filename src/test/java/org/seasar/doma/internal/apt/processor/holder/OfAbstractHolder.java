package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class, factoryMethod = "of")
public abstract class OfAbstractHolder {

  private final int value;

  private OfAbstractHolder(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static OfAbstractHolder of(int value) {
    return new MyHolder(value);
  }

  static class MyHolder extends OfAbstractHolder {

    public MyHolder(int value) {
      super(value);
    }
  }
}
