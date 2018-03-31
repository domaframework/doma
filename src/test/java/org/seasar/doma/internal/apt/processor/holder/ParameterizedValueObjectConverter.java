package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

@ExternalHolder
public class ParameterizedValueObjectConverter
    implements HolderConverter<ParameterizedValueObject<?, ?>, String> {

  @Override
  public String fromHolderToValue(ParameterizedValueObject<?, ?> holder) {
    return null;
  }

  @Override
  public ParameterizedValueObject<?, ?> fromValueToHolder(String value) {
    return null;
  }
}
