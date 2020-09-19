package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.domain.DomainType;

public class DomainResultListParameter<BASIC, DOMAIN>
    extends ScalarResultListParameter<BASIC, DOMAIN> {

  public DomainResultListParameter(DomainType<BASIC, DOMAIN> domainType) {
    super(domainType::createScalar);
  }
}
