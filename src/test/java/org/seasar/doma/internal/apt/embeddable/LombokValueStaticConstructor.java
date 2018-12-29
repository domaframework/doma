package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.lombok.Value;

/** @author nakamura-to */
@Embeddable
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {

  @SuppressWarnings("unused")
  private String street;

  @SuppressWarnings("unused")
  private String city;

  public LombokValueStaticConstructor(String street, String city) {}
}
