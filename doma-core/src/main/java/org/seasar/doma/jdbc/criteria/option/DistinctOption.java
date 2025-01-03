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
package org.seasar.doma.jdbc.criteria.option;

/** Represents the option that decides whether to append the DISTINCT keyword or not. */
@SuppressWarnings("SameReturnValue")
public interface DistinctOption {
  enum Kind implements DistinctOption {
    NONE,
    BASIC
  }

  /**
   * Indicates that the DISTINCT keyword is not required.
   *
   * @return the kind
   */
  static DistinctOption none() {
    return Kind.NONE;
  }

  /**
   * Indicates that the DISTINCT keyword is required.
   *
   * @return the kind
   */
  static DistinctOption basic() {
    return Kind.BASIC;
  }
}
