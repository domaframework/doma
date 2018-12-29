package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;

/** @author taedium */
public class OptionalDomainInOutParameter<BASIC, DOMAIN>
    extends ScalarInOutParameter<BASIC, Optional<DOMAIN>> {

  public OptionalDomainInOutParameter(
      DomainType<BASIC, DOMAIN> domainType, Reference<Optional<DOMAIN>> reference) {
    super(domainType.createOptionalScalar(), reference);
  }
}
