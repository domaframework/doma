package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;

public class DomainOutParameter<BASIC, DOMAIN> extends ScalarOutParameter<BASIC, DOMAIN> {

  public DomainOutParameter(DomainType<BASIC, DOMAIN> domainType, Reference<DOMAIN> reference) {
    super(domainType.createScalar(), reference);
  }
}
