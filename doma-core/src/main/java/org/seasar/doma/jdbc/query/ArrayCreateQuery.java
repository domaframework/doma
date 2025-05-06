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
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

/** A query for creating a SQL array. */
public class ArrayCreateQuery extends AbstractCreateQuery<Array> {

  /** The SQL type name of the array. */
  protected String typeName;

  /** The array elements. */
  protected Object[] elements;

  /**
   * Prepares this query for execution. This method must be called before {@link
   * #create(Connection)}.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(typeName, elements);
  }

  /**
   * Returns the SQL type name of the array.
   *
   * @return the SQL type name
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Sets the SQL type name of the array.
   *
   * @param typeName the SQL type name
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Returns the array elements.
   *
   * @return the array elements
   */
  public Object[] getElements() {
    return elements;
  }

  /**
   * Sets the array elements.
   *
   * @param elements the array elements
   */
  public void setElements(Object[] elements) {
    this.elements = elements;
  }

  /**
   * Creates a SQL array.
   *
   * @param connection the JDBC connection
   * @return the created SQL array
   * @throws SQLException if a database access error occurs
   */
  @Override
  public Array create(Connection connection) throws SQLException {
    return connection.createArrayOf(typeName, elements);
  }
}
