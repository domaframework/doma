package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = String.class, factoryMethod = "of", acceptNull = true)
enum OfJobType {
  SALESEMAN("01"),
  CLERK("02");

  private final String value;

  private OfJobType(String value) {
    this.value = value;
  }

  static OfJobType of(String value) {
    for (OfJobType jobType : OfJobType.values()) {
      if (jobType.value.equals(value)) {
        return jobType;
      }
    }
    return null;
  }

  String getValue() {
    return value;
  }
}
