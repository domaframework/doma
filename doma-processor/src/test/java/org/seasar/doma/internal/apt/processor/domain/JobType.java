package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
enum JobType {
  SALESEMAN("01"),
  CLERK("02");

  private final String value;

  JobType(String value) {
    this.value = value;
  }

  static JobType of(String value) {
    for (JobType jobType : JobType.values()) {
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
