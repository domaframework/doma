package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.domain.DomainType;

public class DomainResultListHandler<BASIC, DOMAIN> extends ScalarResultListHandler<BASIC, DOMAIN> {

  public DomainResultListHandler(DomainType<BASIC, DOMAIN> domainType) {
    super(domainType::createScalar);
    assertNotNull(domainType);
  }
}
