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
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * Indicates that an arbitrary class should be handled as if it was a {@link Domain} annotated class.
 *
 * <p>The annotated class must implement {@link DomainConverter}.
 *
 * <p>In the example below, the SalaryConverter class handles the Salary class as a {@link Domain}
 * annotated class:
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
 * <p>The annotated instance is required to be thread safe.
 *
 * @see DomainConverter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalDomain {}
