package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class)
public class AccessorNotFoundDomain {

  public AccessorNotFoundDomain(BigDecimal value) {}
}
