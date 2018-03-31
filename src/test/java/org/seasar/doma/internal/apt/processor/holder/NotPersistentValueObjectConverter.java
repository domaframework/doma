package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.jdbc.holder.HolderConverter;

/** @author taedium */
@ExternalHolder
public class NotPersistentValueObjectConverter
    implements HolderConverter<NotPersistentValueObject, StringBuilder> {

  @Override
  public StringBuilder fromHolderToValue(NotPersistentValueObject holder) {
    return null;
  }

  @Override
  public NotPersistentValueObject fromValueToHolder(StringBuilder value) {
    return null;
  }
}
