package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.lombok.Value;

/** @author nakamura-to */
@Holder(valueType = boolean.class)
@Value
public class LombokValueAccessorMethod_boolean {

  @SuppressWarnings("unused")
  private boolean value;
}
