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
package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/** A generator that uses a database TABLE. */
public interface TableIdGenerator extends IdGenerator {

  /**
   * Sets the qualified name of the table.
   *
   * @param qualifiedTableName the qualified name of the table
   */
  void setQualifiedTableName(String qualifiedTableName);

  /**
   * Sets the initial value.
   *
   * @param initialValue the initial value
   */
  void setInitialValue(long initialValue);

  /**
   * Sets the allocation size.
   *
   * @param allocationSize the allocation size
   */
  void setAllocationSize(long allocationSize);

  /**
   * Sets the column name of the primary key.
   *
   * @param pkColumnName the column name of the primary key
   */
  void setPkColumnName(String pkColumnName);

  /**
   * Sets the column value of the primary key.
   *
   * @param pkColumnValue the column value of the primary key
   */
  void setPkColumnValue(String pkColumnValue);

  /**
   * Sets the column name of the identity value.
   *
   * @param valueColumnName the column name of the identity value
   */
  void setValueColumnName(String valueColumnName);

  /**
   * Initializes this generator.
   *
   * @throws JdbcException if the initialization is failed
   */
  void initialize();
}
