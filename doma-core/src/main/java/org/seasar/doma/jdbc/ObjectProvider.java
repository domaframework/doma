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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An object provider.
 *
 * <p>The object is mapped to a row of the result set.
 */
public interface ObjectProvider<OBJECT> {

  /**
   * Retrieve the object.
   *
   * @param resultSet the result set
   * @return the object
   * @throws SQLException if the result set throws {@code SQLException}.
   */
  OBJECT get(ResultSet resultSet) throws SQLException;
}
