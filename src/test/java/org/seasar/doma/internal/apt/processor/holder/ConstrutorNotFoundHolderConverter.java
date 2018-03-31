package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.processor.holder.ConstrutorNotFoundHolderConverter.ValueObject;
import org.seasar.doma.jdbc.holder.HolderConverter;

/** @author taedium */
@ExternalHolder
public class ConstrutorNotFoundHolderConverter implements HolderConverter<ValueObject, String> {

  private ConstrutorNotFoundHolderConverter() {}

  @Override
  public String fromHolderToValue(ValueObject holder) {
    return null;
  }

  @Override
  public ValueObject fromValueToHolder(String value) {
    return null;
  }

  static class ValueObject {}
}
