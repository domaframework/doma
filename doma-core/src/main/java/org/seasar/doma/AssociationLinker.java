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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiFunction;

/**
 * Indicates the execution of associations between entities.
 *
 * <p>{@linkplain AssociationLinker} can be annotated on a field of {@link BiFunction}.
 *
 * <p>For the {@linkplain BiFunction}, specify the entity class represented by the {@link
 * #propertyPath()} as the second type parameter. For the first and third type parameters, specify
 * the source entity class associated with the entity class given in the second type parameter.
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
@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssociationLinker {
  /**
   * Specifies the property path from the root entity using dot notation.
   *
   * <p>For example, suppose the root {@code Department} class has a property {@code List<Employee>
   * employeeList}, and {@code Employee} has a property {@code Address address}. In this case, the
   * property path representing {@code address} would be {@code employeeList.address}.
   *
   * @return the property path as a string
   */
  String propertyPath();

  /**
   * Specifies the table alias for the entity class represented by the {@link #propertyPath()}.
   *
   * <p>For example, consider the following SQL:
   *
   * <pre>
   * SELECT * FROM DEPARTMENT d INNER JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT_ID
   * </pre>
   *
   * In this case, the {@code Employee} class corresponds to the {@code EMPLOYEE} table, and its
   * table alias is {@code e}.
   *
   * <p>The table alias concatenated with an underscore serves as the prefix for column aliases.
   *
   * @return the table alias as a string
   */
  String tableAlias();
}
