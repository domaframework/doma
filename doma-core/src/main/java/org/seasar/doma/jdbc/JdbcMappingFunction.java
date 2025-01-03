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

import java.sql.SQLException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * A function that maps between {@link Wrapper} and {@link JdbcType}.
 *
 * <p>The implementation is not required to be thread safe.
 */
public interface JdbcMappingFunction {

  /**
   * Applies this function.
   *
   * @param <R> the return type
   * @param <V> the basic type
   * @param wrapper the wrapper
   * @param jdbcType the JDBC type
   * @return the mapping result
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws SQLException if an SQL related error occurs
   */
  <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException;
}
