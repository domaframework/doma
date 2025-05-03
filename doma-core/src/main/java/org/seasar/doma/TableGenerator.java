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
import org.seasar.doma.jdbc.id.BuiltinTableIdGenerator;
import org.seasar.doma.jdbc.id.TableIdGenerator;

/**
 * Indicates an identifier generator that uses a table.
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
 *     &#064;GeneratedValue(strategy = GenerationType.TABLE)
 *     &#064;TableGenerator(pkColumnValue = &quot;EMPLOYEE_ID&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableGenerator {

  /**
   * @return the catalog name for the generator table.
   */
  String catalog() default "";

  /**
   * @return the schema name for the generator table.
   */
  String schema() default "";

  /**
   * @return the name of the table used for ID generation.
   */
  String table() default "ID_GENERATOR";

  /**
   * @return the name of the column that serves as the primary key in the generator table.
   */
  String pkColumnName() default "PK";

  /**
   * @return the name of the column that stores the generated identifier values.
   */
  String valueColumnName() default "VALUE";

  /**
   * @return the value to be used as the primary key in the generator table to identify this
   *     generator.
   */
  String pkColumnValue();

  /**
   * @return the initial value for the sequence counter in the generator table.
   */
  long initialValue() default 1;

  /**
   * @return the allocation size, which determines how many values are fetched at once from the
   *     generator table.
   */
  long allocationSize() default 1;

  /**
   * @return the implementation class of the {@link TableIdGenerator} interface.
   */
  Class<? extends TableIdGenerator> implementer() default BuiltinTableIdGenerator.class;
}
