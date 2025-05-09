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
package org.seasar.doma.jdbc.command;

/**
 * A functional interface that consumes row index information from a ResultSet. This interface is
 * used to track the current row position and state during ResultSet processing.
 *
 * <p>Implementations of this interface are used by various ResultSet handlers to provide
 * information about the current row being processed and whether there are more rows available.
 */
@FunctionalInterface
public interface ResultSetRowIndexConsumer {

  /**
   * Accepts the current row index and information about whether there are more rows.
   *
   * @param index the current row index (position) in the ResultSet, can be null
   * @param next indicates whether there are more rows available in the ResultSet, can be null
   */
  void accept(Long index, Boolean next);
}
