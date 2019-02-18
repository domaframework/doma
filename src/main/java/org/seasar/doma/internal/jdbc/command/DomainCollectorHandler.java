package org.seasar.doma.internal.jdbc.command;

import java.util.stream.Collector;
import org.seasar.doma.jdbc.domain.DomainType;

public class DomainCollectorHandler<BASIC, DOMAIN, RESULT>
    extends ScalarCollectorHandler<BASIC, DOMAIN, RESULT> {

  public DomainCollectorHandler(
      DomainType<BASIC, DOMAIN> domainType, Collector<DOMAIN, ?, RESULT> collector) {
    super(() -> domainType.createScalar(), collector);
  }
}
