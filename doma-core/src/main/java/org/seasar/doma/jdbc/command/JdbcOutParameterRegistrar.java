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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * A function that registers output parameters for a callable statement.
 *
 * <p>This class is used to register output parameters for stored procedures and functions. It
 * implements {@link JdbcMappingFunction} to handle different JDBC types.
 */
public class JdbcOutParameterRegistrar implements JdbcMappingFunction {

  /** The callable statement to register output parameters for. */
  protected final CallableStatement callableStatement;

  /** The parameter index in the callable statement. */
  protected final int index;

  /**
   * Creates a new instance.
   *
   * @param callableStatement the callable statement to register output parameters for
   * @param index the parameter index in the callable statement
   * @throws NullPointerException if the callable statement is null
   * @throws IllegalArgumentException if the index is less than or equal to 0
   */
  public JdbcOutParameterRegistrar(CallableStatement callableStatement, int index) {
    assertNotNull(callableStatement);
    assertTrue(index > 0, index);
    this.callableStatement = callableStatement;
    this.index = index;
  }

  /**
   * Applies this function to register an output parameter.
   *
   * @param <R> the result type
   * @param <V> the value type
   * @param wrapper the wrapper for the value
   * @param jdbcType the JDBC type
   * @return always null
   * @throws SQLException if a database access error occurs
   * @throws DomaNullPointerException if the jdbcType is null
   */
  @Override
  public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
    if (jdbcType == null) {
      throw new DomaNullPointerException("jdbcType");
    }
    jdbcType.registerOutParameter(callableStatement, index);
    return null;
  }
}
