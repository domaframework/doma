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
 * Indicates an aggregation of {@link DomainConverter} classes.
 *
 * <p>The full qualified name of the annotated class must be specified in annotation processing
 * options. The option key is {@code doma.domain.converters}.
 *
 * <pre>
 * &#064;DomainConverters({ SalaryConverter.class, DayConverter.class, LocationConverter.class })
 * public class DomainConvertersProvider {
 * }
 * </pre>
 *
 * @see DomainConverter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainConverters {

  /**
   * @return the array of {@code DomainConverter} classes that should be registered for domain type conversion.
   */
  Class<? extends DomainConverter<?, ?>>[] value() default {};
}
