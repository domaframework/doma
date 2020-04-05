package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import org.seasar.doma.jdbc.domain.DomainType;

public class OptionalDomainResultListHandler<BASIC, DOMAIN>
    extends ScalarResultListHandler<BASIC, Optional<DOMAIN>> {

  public OptionalDomainResultListHandler(DomainType<BASIC, DOMAIN> domainType) {
    super(() -> domainType.createOptionalScalar());
    assertNotNull(domainType);
  }
}
