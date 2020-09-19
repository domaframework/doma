package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import org.seasar.doma.jdbc.domain.DomainType;

public class OptionalDomainResultListParameter<BASIC, DOMAIN>
    extends ScalarResultListParameter<BASIC, Optional<DOMAIN>> {

  public OptionalDomainResultListParameter(DomainType<BASIC, DOMAIN> domainType) {
    super(domainType::createOptionalScalar);
  }
}
