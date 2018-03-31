package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.internal.apt.processor.holder.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.jdbc.holder.HolderConverter;

@ExternalHolder
public class NestingValueObjectConverter implements HolderConverter<NestingValueObject, String> {

  public static class NestingValueObject {}

  @Override
  public String fromHolderToValue(NestingValueObject holder) {
    return null;
  }

  @Override
  public NestingValueObject fromValueToHolder(String value) {
    return null;
  }
}
