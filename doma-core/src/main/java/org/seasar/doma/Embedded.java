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
 * Indicates an embedded property.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. The type of the
 * field must be a class annotated with {@link Embeddable}.
 *
 * <p>The {@link Embedded} annotation allows for embedding value objects within entities, enabling
 * composition of entities from smaller, reusable components.
 *
 * <pre>
 * &#064;Embeddable
 * public class Address {
 *     String street;
 *     String city;
 *     String zipCode;
 * }
 *
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     Integer id;
 *
 *     String name;
 *
 *     &#064;Embedded
 *     Address address;
 *
 *     &#064;Embedded(prefix = "home_")
 *     Address homeAddress;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Embedded {

  /**
   * The prefix for column names of the embedded properties.
   *
   * <p>When specified, the prefix is prepended to the column names of all properties within the
   * embedded object. This is useful when embedding the same embeddable type multiple times in an
   * entity.
   *
   * <p>For example, if an embeddable has a property mapped to column "street" and the prefix is
   * "home_", the resulting column name will be "home_street".
   *
   * @return the column name prefix
   */
  String prefix() default "";
}
