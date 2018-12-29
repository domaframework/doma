package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
class PackagePrivateDomain {

  private final int value;

  PackagePrivateDomain(int value) {
    this.value = value;
  }

  int getValue() {
    return value;
  }
}
