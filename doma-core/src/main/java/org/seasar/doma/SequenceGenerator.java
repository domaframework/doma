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
import org.seasar.doma.jdbc.id.BuiltinSequenceIdGenerator;
import org.seasar.doma.jdbc.id.SequenceIdGenerator;

/**
 * Indicates an identifier generator that uses a sequence.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. This annotation
 * must be used in conjunction with the {@link Id} annotation and the {@link GeneratedValue}
 * annotation.
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
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceGenerator {

  /**
   * @return the catalog name for the sequence.
   */
  String catalog() default "";

  /**
   * @return the schema name for the sequence.
   */
  String schema() default "";

  /**
   * @return the name of the database sequence to use for generating identifiers.
   */
  String sequence();

  /**
   * @return the initial value for the sequence.
   */
  long initialValue() default 1;

  /**
   * @return the allocation size for the sequence, which determines how many values are fetched at
   *     once.
   */
  long allocationSize() default 1;

  /**
   * @return the implementation class of the {@link SequenceIdGenerator} interface.
   */
  Class<? extends SequenceIdGenerator> implementer() default BuiltinSequenceIdGenerator.class;
}
