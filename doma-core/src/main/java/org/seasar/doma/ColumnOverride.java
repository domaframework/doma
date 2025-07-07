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

/**
 * Overrides the column mapping for a specific property within an embeddable object.
 *
 * <p>This annotation is used within the {@link Embedded} annotation to customize how properties of
 * an embeddable object are mapped to database columns. It allows you to override the default column
 * name and attributes (such as insertable, updatable, and quote) for individual properties when the
 * embeddable is embedded in an entity.
 *
 * <p>This is particularly useful when:
 *
 * <ul>
 *   <li>The same embeddable type is used multiple times in an entity and requires different column
 *       names to avoid conflicts
 *   <li>The column names in the database don't match the default naming conventions
 *   <li>You need to control the behavior of specific columns (e.g., making them read-only)
 * </ul>
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
 *     &#064;Id
 *     Integer id;
 *
 *     &#064;Embedded(columnOverrides = {
 *         &#064;ColumnOverride(name = "street", column = &#064;Column(name = "HOME_STREET")),
 *         &#064;ColumnOverride(name = "city", column = &#064;Column(name = "HOME_CITY")),
 *         &#064;ColumnOverride(name = "zipCode", column = &#064;Column(name = "HOME_ZIP"))
 *     })
 *     Address homeAddress;
 *
 *     &#064;Embedded(columnOverrides = {
 *         &#064;ColumnOverride(name = "street", column = &#064;Column(name = "WORK_STREET")),
 *         &#064;ColumnOverride(name = "city", column = &#064;Column(name = "WORK_CITY")),
 *         &#064;ColumnOverride(name = "zipCode", column = &#064;Column(name = "WORK_ZIP"))
 *     })
 *     Address workAddress;
 * }
 * </pre>
 *
 * @see Embedded
 * @see Column
 * @see Embeddable
 */
public @interface ColumnOverride {
  /**
   * The name of the property in the embeddable class whose column mapping should be overridden.
   *
   * <p>This must match exactly the name of a field in the embeddable class.
   *
   * @return the property name in the embeddable class
   */
  String name();

  /**
   * The column definition that overrides the default column mapping for the specified property.
   *
   * <p>This allows you to specify a custom column name and control column attributes such as
   * insertable, updatable, and quote.
   *
   * @return the column definition to use for the property
   */
  Column column();
}
