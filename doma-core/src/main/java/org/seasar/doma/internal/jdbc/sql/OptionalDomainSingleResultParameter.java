package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import org.seasar.doma.jdbc.domain.DomainType;

public class OptionalDomainSingleResultParameter<BASIC, DOMAIN>
    extends ScalarSingleResultParameter<BASIC, Optional<DOMAIN>> {

  public OptionalDomainSingleResultParameter(DomainType<BASIC, DOMAIN> domainType) {
    super(domainType.createOptionalScalar());
  }
}
