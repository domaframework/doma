package org.seasar.doma.jdbc.domain;

import org.seasar.doma.DomainConverters;
import org.seasar.doma.ExternalDomain;

/**
 * A converter between domain objects and basic values.
 *
 * <p>The implementation class should be annotated with {@link ExternalDomain} and be registered to
 * {@link DomainConverters}.
 *
 * <pre>
 * &#064;ExtenalDomain
 * public class SalaryConverter implements DomainConverter&lt;Salary, BigDecimal&gt; {
 *
 *     public BigDecimal fromDomainToValue(Salary domain) {
 *         return domain.getValue();
 *     }
 *
 *     public Salary fromValueToDomain(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 *
 * @see ExternalDomain
 * @see DomainConverters
 * @param <DOMAIN> the domain type
 * @param <BASIC> the basic type
 */
public interface DomainConverter<DOMAIN, BASIC> {

  /**
   * Converts from a domain object to a basic value.
   *
   * @param domain the domain object
   * @return the basic value
   */
  BASIC fromDomainToValue(DOMAIN domain);

  /**
   * Converts from a basic value to a domain object.
   *
   * @param value the basic value
   * @return the domain object
   */
  DOMAIN fromValueToDomain(BASIC value);
}
