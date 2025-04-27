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
 * Indicates that specific properties of an entity should be included or excluded in the context of
 * a {@link Dao} operation.
 *
 * <ul>
 *   <li>These attributes are optional and can be used together.
 *   <li>When neither attribute is specified, all properties are included by default.
 * </ul>
 */
public @interface Returning {
  /**
   * Specifies the names of properties to be explicitly included. If specified, only the listed
   * properties will be included.
   *
   * @return an array of property names to include
   */
  String[] include() default {};

  /**
   * Specifies the names of properties to be explicitly excluded. If specified, all properties will
   * be included except for those listed.
   *
   * @return an array of property names to exclude
   */
  String[] exclude() default {};
}
