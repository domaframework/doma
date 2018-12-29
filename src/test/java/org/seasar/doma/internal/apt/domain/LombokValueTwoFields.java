package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.lombok.Value;

/** @author nakamura-to */
@Domain(valueType = String.class)
@Value
public class LombokValueTwoFields {

  @SuppressWarnings("unused")
  private String value1;

  @SuppressWarnings("unused")
  private String value2;
}
