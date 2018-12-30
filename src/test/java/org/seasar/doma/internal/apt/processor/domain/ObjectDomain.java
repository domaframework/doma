package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Object.class)
public final class ObjectDomain {

  public ObjectDomain(final Object o) {}

  public Object getValue() {
    return null;
  }
}
