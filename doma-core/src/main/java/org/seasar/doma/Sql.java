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
 * Indicates a SQL template for database operations.
 *
 * <p>This annotation can be combined with following annotations:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.BatchDelete}
 *   <li>{@link org.seasar.doma.BatchInsert}
 *   <li>{@link org.seasar.doma.BatchUpdate}
 *   <li>{@link org.seasar.doma.Delete}
 *   <li>{@link org.seasar.doma.Insert}
 *   <li>{@link org.seasar.doma.Update}
 *   <li>{@link org.seasar.doma.Script}
 *   <li>{@link org.seasar.doma.Select}
 *   <li>{@link org.seasar.doma.SqlProcessor}
 * </ul>
 *
 * <pre>
 * &#064;Dao
 * public interface EmployeeDao {
 *
 *     &#064;Sql("select name from employee where age = &#047;* age *&#047;0)")
 *     &#064;Select
 *     List&lt;String&gt; selectNamesByAge(int age);
 *
 *     &#064;Sql("insert into employee (name) values (&#047;* employee.name *&#047;'test data')")
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

  /**
   * @return the SQL template
   */
  String value();
}
