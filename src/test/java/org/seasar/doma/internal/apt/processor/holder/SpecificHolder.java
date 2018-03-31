package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

/** @author taedium */
@Holder(valueType = Integer.class, acceptNull = true)
public final class SpecificHolder extends GenericHolder<Integer> {

  public SpecificHolder(final Integer value) {
    super(value);
  }
}
