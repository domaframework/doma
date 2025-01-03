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

/** An identity provider. */
@Deprecated
public interface IdProvider {

  /**
   * Whether this provider can return the identity value.
   *
   * @return {@code true} if this provider can return the identity value
   */
  boolean isAvailable();

  /**
   * Provides the identity value.
   *
   * @return the identity value
   * @throws IllegalStateException if an illegal state is detected
   * @throws UnsupportedOperationException if this method is not supported
   */
  long get();
}
