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

/**
 * Indicates a metamodel class that provides type-safe access to entity properties.
 *
 * <p>Metamodel classes enable compile-time type checking when referencing entity properties
 * in criteria queries and other database operations.
 *
 * <p>For example, the name of the metamodel is "MyEmployeeMetamodel" when you specify the prefix
 * and the suffix as follows:
 *
 * <pre>
 * &#064;Entity(metamodel = &#064;Metamodel(prefix = "My", suffix="Metamodel")
 * public class Employee {
 *     ...
 * }
 * </pre>
 *
 * <p>If both the {@code prefix} and the {@code suffix} are empty, the values of the following
 * annotation processing options are used:
 *
 * <ul>
 *   <li>doma.metamodel.prefix
 *   <li>doma.metamodel.suffix
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Metamodel {
  /**
   * @return the prefix for the metamodel class
   */
  String prefix() default "";

  /**
   * @return the suffix for the metamodel class
   */
  String suffix() default "";

  /**
   * @return the array of scope classes that define the visibility boundaries for this metamodel
   */
  Class<?>[] scopes() default {};
}
