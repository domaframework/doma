package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class, factoryMethod = "of")
enum OfPrimitiveValueType {
  INT(1),
  DOUBLE(2);

  private final int value;

  private OfPrimitiveValueType(int value) {
    this.value = value;
  }

  static OfPrimitiveValueType of(int value) {
    for (OfPrimitiveValueType primitiveValue : OfPrimitiveValueType.values()) {
      if (primitiveValue.value == value) {
        return primitiveValue;
      }
    }
    return null;
  }

  int getValue() {
    return value;
  }
}
