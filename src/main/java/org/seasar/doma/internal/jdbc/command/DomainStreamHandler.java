package org.seasar.doma.internal.jdbc.command;

import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <DOMAIN>
 */
public class DomainStreamHandler<BASIC, DOMAIN, RESULT>
    extends ScalarStreamHandler<BASIC, DOMAIN, RESULT> {

  public DomainStreamHandler(
      DomainType<BASIC, DOMAIN> domainType, Function<Stream<DOMAIN>, RESULT> mapper) {
    super(() -> domainType.createScalar(), mapper);
  }
}
