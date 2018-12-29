package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.Optional;
import org.seasar.doma.jdbc.domain.DomainType;

/** @author taedium */
public class OptionalDomainListParameter<BASIC, DOMAIN>
    extends ScalarListParameter<BASIC, Optional<DOMAIN>> {

  public OptionalDomainListParameter(
      DomainType<BASIC, DOMAIN> domainType, List<Optional<DOMAIN>> list, String name) {
    super(() -> domainType.createOptionalScalar(), list, name);
  }
}
