package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.jdbc.holder.HolderConverter;

// @ExternalHolder
public class TuesdayConverter implements HolderConverter<Tuesday, String> {

  @Override
  public String fromHolderToValue(Tuesday holder) {
    return null;
  }

  @Override
  public Tuesday fromValueToHolder(String value) {
    return null;
  }
}
