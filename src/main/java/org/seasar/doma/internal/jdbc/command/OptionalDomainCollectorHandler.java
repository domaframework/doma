package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <DOMAIN>
 */
public class OptionalDomainCollectorHandler<BASIC, DOMAIN, RESULT>
    extends ScalarCollectorHandler<BASIC, Optional<DOMAIN>, RESULT> {

  public OptionalDomainCollectorHandler(
      DomainType<BASIC, DOMAIN> domainType, Collector<Optional<DOMAIN>, ?, RESULT> collector) {
    super(() -> domainType.createOptionalScalar(), collector);
  }
}
