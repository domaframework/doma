package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.domain.DomainType;

/** @author taedium */
public class DomainInParameter<BASIC, DOMAIN> extends ScalarInParameter<BASIC, DOMAIN> {

  public DomainInParameter(DomainType<BASIC, DOMAIN> domainType, DOMAIN domain) {
    super(domainType.createScalar(), domain);
  }
}
