package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

/** @author taedium */
@ExternalHolder
public class VersionNoConverter implements HolderConverter<VersionNo, Integer> {

  @Override
  public Integer fromHolderToValue(VersionNo holder) {
    return null;
  }

  @Override
  public VersionNo fromValueToHolder(Integer value) {
    return null;
  }
}
