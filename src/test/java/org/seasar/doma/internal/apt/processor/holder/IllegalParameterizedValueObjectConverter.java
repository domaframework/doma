package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

@ExternalHolder
public class IllegalParameterizedValueObjectConverter
    implements HolderConverter<ParameterizedValueObject<?, String>, String> {

  @Override
  public String fromHolderToValue(ParameterizedValueObject<?, String> holder) {
    return null;
  }

  @Override
  public ParameterizedValueObject<?, String> fromValueToHolder(String value) {
    return null;
  }
}
