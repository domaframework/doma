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

/**
 * Indicates a strategy to generate identifiers.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class and the field must
 * be annotated with {@link Id}.
 *
 * <p>The additional annotation is required according to the {@code strategy} value:
 *
 * <ul>
 *   <li>the {@link SequenceGenerator} annotation is required, if {@link GenerationType#SEQUENCE} is
 *       specified.
 *   <li>the {@link TableGenerator} annotation is required, if {@link GenerationType#TABLE} is
 *       specified.
 * </ul>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.SEQUENCE)
 *     &#064;SequenceGenerator(sequence = &quot;EMPLOYEE_SEQ&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 *
 * @see GenerationType
 * @see SequenceGenerator
 * @see TableGenerator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

  /**
   * @return the strategy for generating identifiers.
   */
  GenerationType strategy();
}
