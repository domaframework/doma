package org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.type.JdbcType;

/**
 * A JDBC type provider for a domain type.
 *
 * @param <DOMAIN> the domain type
 */
public abstract class JdbcTypeProvider<DOMAIN> implements DomainConverter<DOMAIN, Object> {

  @Override
  public final Object fromDomainToValue(DOMAIN domain) {
    return domain;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final DOMAIN fromValueToDomain(Object value) {
    return (DOMAIN) value;
  }

  /**
   * Returns the JDBC type.
   *
   * @return the JDBC type
   */
  public abstract JdbcType<DOMAIN> getJdbcType();
}
