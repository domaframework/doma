package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = Object.class)
public final class ObjectHolder {

  public ObjectHolder(final Object o) {}

  public Object getValue() {
    return null;
  }
}
