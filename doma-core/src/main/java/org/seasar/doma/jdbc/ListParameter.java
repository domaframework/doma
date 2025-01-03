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
package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.query.Query;

/** A list parameter. */
public interface ListParameter<ELEMENT> extends SqlParameter {

  /**
   * Returns the parameter name.
   *
   * @return the parameter name
   */
  String getName();

  /**
   * Creates the object provider that provides list elements.
   *
   * @param query the query
   * @return the object provider
   */
  ObjectProvider<ELEMENT> createObjectProvider(Query query);

  /**
   * Adds the list element.
   *
   * @param element the list element
   */
  void add(ELEMENT element);
}
