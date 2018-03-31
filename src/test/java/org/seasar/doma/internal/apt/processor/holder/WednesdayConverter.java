package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

@ExternalHolder
public class WednesdayConverter implements HolderConverter<Wednesday, String> {

  @Override
  public String fromHolderToValue(Wednesday holder) {
    return null;
  }

  @Override
  public Wednesday fromValueToHolder(String value) {
    return null;
  }
}
