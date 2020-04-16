package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.internal.jdbc.command.ScalarProvider;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.query.Query;

public class DomainListParameter<BASIC, DOMAIN> extends AbstractListParameter<DOMAIN> {

  protected final DomainType<BASIC, DOMAIN> domainType;

  public DomainListParameter(DomainType<BASIC, DOMAIN> domainType, List<DOMAIN> list, String name) {
    super(list, name);
    assertNotNull(domainType);
    this.domainType = domainType;
  }

  @Override
  public ScalarProvider<BASIC, DOMAIN> createObjectProvider(Query query) {
    return new ScalarProvider<BASIC, DOMAIN>(() -> domainType.createScalar(), query);
  }
}
