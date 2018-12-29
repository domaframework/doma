package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = String.class)
enum JobType {
  SALESEMAN("01"),
  CLERK("02");

  private final String value;

  private JobType(String value) {
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
