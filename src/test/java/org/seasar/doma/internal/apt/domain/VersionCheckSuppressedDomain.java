package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = BigDecimal.class, acceptNull = true)
public class VersionCheckSuppressedDomain {

  private final BigDecimal value;

  public VersionCheckSuppressedDomain(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
