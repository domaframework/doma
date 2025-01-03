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

/**
 * A commenter for SQL strings.
 *
 * <p>The comment is added to the SQL string that may be built from a SQL template or may be auto
 * generated.
 *
 * <p>The implementation must not add anything except a comment.
 */
public interface Commenter {

  /**
   * Adds the comment to the SQL string.
   *
   * @param sql the SQL string
   * @param context the context
   * @return the SQL that the comment is added
   */
  default String comment(String sql, CommentContext context) {
    return sql;
  }
}
