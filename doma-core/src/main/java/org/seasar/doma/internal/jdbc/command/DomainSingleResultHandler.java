package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.domain.DomainType;

public class DomainSingleResultHandler<BASIC, DOMAIN>
    extends ScalarSingleResultHandler<BASIC, DOMAIN> {

  public DomainSingleResultHandler(DomainType<BASIC, DOMAIN> domainType) {
    super(() -> domainType.createScalar());
    assertNotNull(domainType);
  }
}
