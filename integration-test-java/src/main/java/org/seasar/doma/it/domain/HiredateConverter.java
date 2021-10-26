package org.seasar.doma.it.domain;

import java.sql.Date;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class HiredateConverter implements DomainConverter<Hiredate, Date> {

  @Override
  public Date fromDomainToValue(Hiredate domain) {
    return domain.getValue();
  }

  @Override
  public Hiredate fromValueToDomain(Date value) {
    return new HiredateImpl(value);
  }
}
