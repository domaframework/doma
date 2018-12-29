package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.lombok.Value;

@Domain(valueType = boolean.class)
@Value
public class LombokValueAccessorMethod_boolean {

  @SuppressWarnings("unused")
  private boolean value;
}
