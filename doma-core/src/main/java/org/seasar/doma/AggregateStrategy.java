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
 * Indicates a strategy for aggregating database records.
 *
 * <p>The element annotated with {@linkplain AggregateStrategy} must be an interface.
 *
 * <pre>
 * &#064;AggregateStrategy(root = Department.class, tableAlias = "d")
 * interface DepartmentStrategy {
 *
 *     &#064;AssociationLinker(propertyPath = "employeeList", tableAlias = "e")
 *     BiFunction&lt;Department, Employee, Department&gt; employeeList = (d, e) -> {
 *         d.getEmployeeList().add(e);
 *         e.setDepartment(d);
 *         return d;
 *     };
 *
 *     &#064;AssociationLinker(propertyPath = "employeeList.address", tableAlias = "a")
 *     BiFunction&lt;Employee, Address, Employee&gt; employeeListAddress = (e, a) -> {
 *         e.setAddress(a);
 *         return e;
 *     };
 * }
 * </pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateStrategy {

  /**
   * Specifies the root entity class associated with the aggregation strategy.
   *
   * @return the root class
   */
  Class<?> root();

  /**
   * Specifies the root table alias.
   *
   * @return the alias name for a root table
   */
  String tableAlias();
}
