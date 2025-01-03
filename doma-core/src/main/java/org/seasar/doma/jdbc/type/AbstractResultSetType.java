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
package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;

/** A JDBC type for RDBMS specific SQL type and {@link ResultSet}. */
public abstract class AbstractResultSetType extends AbstractJdbcType<ResultSet> {

  /**
   * Creates an instance.
   *
   * @param type RDBMS specific SQL type
   */
  protected AbstractResultSetType(int type) {
    super(type);
  }

  /**
   * @throws JdbcUnsupportedOperationException if invoked
   */
  @Override
  public ResultSet getValue(ResultSet resultSet, int index) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "getValue");
  }

  /**
   * @throws JdbcUnsupportedOperationException if invoked
   */
  @Override
  public void setValue(PreparedStatement preparedStatement, int index, ResultSet value) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "setValue");
  }

  /**
   * @return {@code null}
   */
  @Override
  protected ResultSet doGetValue(ResultSet resultSet, int index) {
    return null;
  }

  /** Does nothing. */
  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, ResultSet value) {}

  @Override
  protected ResultSet doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    Object resultSet = callableStatement.getObject(index);
    return (ResultSet) resultSet;
  }

  @Override
  protected String doConvertToLogFormat(ResultSet value) {
    return value.toString();
  }
}
