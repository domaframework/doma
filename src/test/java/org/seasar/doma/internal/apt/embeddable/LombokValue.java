package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.lombok.Value;

@Embeddable
@Value
public class LombokValue {

  @SuppressWarnings("unused")
  private String street;

  @SuppressWarnings("unused")
  private String city;

  public LombokValue(String street, String city) {}
}
