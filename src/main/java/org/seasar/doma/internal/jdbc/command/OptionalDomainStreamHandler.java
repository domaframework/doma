package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <DOMAIN>
 */
public class OptionalDomainStreamHandler<BASIC, DOMAIN, RESULT>
    extends ScalarStreamHandler<BASIC, Optional<DOMAIN>, RESULT> {

  public OptionalDomainStreamHandler(
      DomainType<BASIC, DOMAIN> domainType, Function<Stream<Optional<DOMAIN>>, RESULT> mapper) {
    super(() -> domainType.createOptionalScalar(), mapper);
  }
}
