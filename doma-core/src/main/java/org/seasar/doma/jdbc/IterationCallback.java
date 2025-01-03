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

import java.sql.ResultSet;

/**
 * A callback that is invoked from a {@link ResultSet} iteration process.
 *
 * <p>The implementation class is not required to be thread safe.
 *
 * @param <TARGET> the mapping target type
 * @param <RESULT> the result type
 */
public interface IterationCallback<TARGET, RESULT> {

  /**
   * Returns the default value.
   *
   * @return the default value
   */
  default RESULT defaultResult() {
    return null;
  }

  /**
   * Invoked from a {@link ResultSet} iteration process.
   *
   * @param target the object that is mapped to a row of {@link ResultSet}
   * @param context the execution context
   * @return the result type
   */
  RESULT iterate(TARGET target, IterationContext context);

  /**
   * Executes post process of {@link #iterate(Object, IterationContext)}.
   *
   * @param result the result of {@link #iterate(Object, IterationContext)} or the result of {@link
   *     #defaultResult()} if {@link ResultSet} has no rows
   * @param context the execution context
   * @return the result
   */
  default RESULT postIterate(RESULT result, IterationContext context) {
    return result;
  }
}
