package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import org.seasar.doma.jdbc.domain.DomainType;

public class OptionalDomainInParameter<BASIC, DOMAIN>
    extends ScalarInParameter<BASIC, Optional<DOMAIN>> {

  public OptionalDomainInParameter(DomainType<BASIC, DOMAIN> domainType, Optional<DOMAIN> value) {
    super(domainType.createOptionalScalar(), value);
  }
}
