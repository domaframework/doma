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

/**
 * Specifies how to handle duplicate key violations during insert operations.
 *
 * <p>This enum defines the behavior when an insert operation would violate a unique constraint in
 * the database.
 */
public enum DuplicateKeyType {
  /**
   * Updates the existing row when a duplicate key is encountered.
   *
   * <p>This option causes the database to perform an update on the existing row instead of inserting
   * a new row when a duplicate key is detected.
   */
  UPDATE,

  /**
   * Ignores the insert operation when a duplicate key is encountered.
   *
   * <p>This option causes the database to silently ignore the insert operation without raising an
   * error when a duplicate key is detected.
   */
  IGNORE,

  /**
   * Throws an exception when a duplicate key is encountered.
   *
   * <p>This option causes the database to raise an error when a duplicate key is detected, which is
   * the default behavior for most databases.
   */
  EXCEPTION,
  ;
}
