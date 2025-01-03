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

import org.seasar.doma.wrapper.Wrapper;

/**
 * A function that converts the {@link Wrapper} values to the SQL log formats.
 *
 * <p>The implementation class is not required to be thread safe.
 */
public interface SqlLogFormattingFunction {

  /**
   * Apply this function.
   *
   * @param <V> the basic type
   * @param wrapper the wrapper value
   * @param formatter the formatter
   * @return the converted string
   */
  <V> String apply(Wrapper<V> wrapper, SqlLogFormatter<V> formatter);
}
