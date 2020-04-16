package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;

public class DomainInOutParameter<BASIC, DOMAIN> extends ScalarInOutParameter<BASIC, DOMAIN> {

  public DomainInOutParameter(DomainType<BASIC, DOMAIN> domainType, Reference<DOMAIN> reference) {
    super(domainType.createScalar(), reference);
  }
}
