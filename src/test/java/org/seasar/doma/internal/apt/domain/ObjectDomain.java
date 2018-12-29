package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

/** @author nakamura-to */
@Domain(valueType = Object.class)
public final class ObjectDomain {

  public ObjectDomain(final Object o) {}

  public Object getValue() {
    return null;
  }
}
