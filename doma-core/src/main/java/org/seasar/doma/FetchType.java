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
 * Defines how to fetch rows from an SQL SELECT result set.
 *
 * <p>This enum specifies the strategy for retrieving rows from a database query result, balancing
 * memory usage against database connection time.
 */
public enum FetchType {

  /**
   * Fetches all rows immediately.
   *
   * <p>More memory usage and less Database connected time.
   */
  EAGER,

  /**
   * Fetches rows gradually.
   *
   * <p>Less memory usage and more Database connected time.
   */
  LAZY
}
