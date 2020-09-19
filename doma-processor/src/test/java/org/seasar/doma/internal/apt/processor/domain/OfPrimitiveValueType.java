package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class, factoryMethod = "of")
enum OfPrimitiveValueType {
  INT(1),
  DOUBLE(2);

  private final int value;

  OfPrimitiveValueType(int value) {
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
