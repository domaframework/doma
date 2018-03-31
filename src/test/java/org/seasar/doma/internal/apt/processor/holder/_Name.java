package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

/** @author taedium */
public class _Name extends AbstractHolderDesc<String, Name> {

  private _Name() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public Name newHolder(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Name holder) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return null;
  }

  @Override
  public Class<Name> getHolderClass() {
    return null;
  }

  public static _Name getSingletonInternal() {
    return null;
  }
}
