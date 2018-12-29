package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = Integer.class, acceptNull = true)
public final class SpecificDomain extends GenericDomain<Integer> {

  public SpecificDomain(final Integer value) {
    super(value);
  }
}
