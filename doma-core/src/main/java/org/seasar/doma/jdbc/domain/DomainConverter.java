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

import org.seasar.doma.ExternalDomain;

/**
 * A converter interface for mapping between domain objects and their corresponding basic values.
 *
 * <p>The implementation class should be annotated with {@link ExternalDomain}.
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
 *         if (value == null) {
 *             return null;
 *         }
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 *
 * @see ExternalDomain
 * @param <DOMAIN> the type of the domain object
 * @param <BASIC> the type of the basic value
 */
public interface DomainConverter<DOMAIN, BASIC> {

  /**
   * Converts a domain object into a basic value.
   *
   * @param domain the domain object; must not be null
   * @return the basic value; can be null
   */
  BASIC fromDomainToValue(DOMAIN domain);

  /**
   * Converts a basic value into a domain object.
   *
   * @param value the basic value; can be null
   * @return the domain object; can be null
   */
  DOMAIN fromValueToDomain(BASIC value);
}
