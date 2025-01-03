/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * &#064;ExternalDomain
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
