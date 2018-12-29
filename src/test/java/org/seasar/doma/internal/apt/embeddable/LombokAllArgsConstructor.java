package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class LombokAllArgsConstructor {

  @SuppressWarnings("unused")
  private String street;

  @SuppressWarnings("unused")
  private String city;

  public LombokAllArgsConstructor(String street, String city) {}
}
