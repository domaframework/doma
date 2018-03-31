package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

/** @author taedium */
@ExternalHolder
public class PrimaryKeyConverter implements HolderConverter<PrimaryKey, Integer> {

  @Override
  public Integer fromHolderToValue(PrimaryKey holder) {
    return null;
  }

  @Override
  public PrimaryKey fromValueToHolder(Integer value) {
    return null;
  }
}
