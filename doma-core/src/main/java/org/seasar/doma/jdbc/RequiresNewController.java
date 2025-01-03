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

import org.seasar.doma.DomaNullPointerException;

/**
 * A controller for the transaction whose attribute may be {@literal REQUIRES_NEW}.
 *
 * <p>This object may increment identity values efficiently in a {@literal REQUIRES_NEW}
 * transaction.
 *
 * <p>The implementation class must be thread safe.
 */
public interface RequiresNewController {

  /**
   * Executes a {@literal REQUIRES_NEW} transaction.
   *
   * @param <R> the result type
   * @param callback the callback that is executed in a {@literal REQUIRES_NEW} transaction
   * @return the result
   * @throws DomaNullPointerException if ｛@code callback} is {@code null}
   * @throws Throwable thrown, if any exception is thrown in the｛@code callback} execution
   */
  @SuppressWarnings("RedundantThrows")
  default <R> R requiresNew(Callback<R> callback) throws Throwable {
    if (callback == null) {
      throw new DomaNullPointerException("callback");
    }
    return callback.execute();
  }

  /**
   * A callback that is executed in a {@literal REQUIRES_NEW} transaction.
   *
   * @param <R> the result type
   */
  interface Callback<R> {

    /**
     * Executes this callback.
     *
     * @return the result
     */
    R execute();
  }
}
