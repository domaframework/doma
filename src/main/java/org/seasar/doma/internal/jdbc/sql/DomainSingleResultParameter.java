package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.domain.DomainType;

/** @author taedium */
public class DomainSingleResultParameter<BASIC, DOMAIN>
    extends ScalarSingleResultParameter<BASIC, DOMAIN> {

  public DomainSingleResultParameter(DomainType<BASIC, DOMAIN> domainType) {
    super(domainType.createScalar());
  }
}
