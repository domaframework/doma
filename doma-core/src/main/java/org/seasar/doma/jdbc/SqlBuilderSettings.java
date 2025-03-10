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

/** A context for SQL builder settings. */
public interface SqlBuilderSettings {

  /**
   * Returns whether the block comment should be removed. The beginning of the {@code comment}
   * parameter string must always start with "/*".
   *
   * @param comment the block comment, never null
   * @return true if the block comment should be removed
   */
  default boolean shouldRemoveBlockComment(String comment) {
    return false;
  }

  /**
   * Returns whether the line comment should be removed. The beginning of the {@code comment}
   * parameter string must always start with "--".
   *
   * @param comment the line comment, never null
   * @return true if the line comment should be removed
   */
  default boolean shouldRemoveLineComment(String comment) {
    return false;
  }

  /**
   * Returns whether the blank lines should be removed.
   *
   * @return true if the blank lines should be removed
   */
  default boolean shouldRemoveBlankLines() {
    return false;
  }

  /**
   * Determines whether padding is required for elements in an "IN" list in SQL queries.
   *
   * @return true if padding is required for elements in an "IN" list, false otherwise
   */
  default boolean shouldRequireInListPadding() {
    return false;
  }
}
