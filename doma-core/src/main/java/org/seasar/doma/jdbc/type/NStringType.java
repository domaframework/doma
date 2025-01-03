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

import java.sql.*;

/** A JDBC type for {@link Types#NVARCHAR} and {@link String}. */
public class NStringType extends AbstractJdbcType<String> {

  public NStringType() {
    super(Types.NVARCHAR);
  }

  @Override
  protected String doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getNString(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, String value)
      throws SQLException {
    preparedStatement.setNString(index, value);
  }

  @Override
  protected String doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getNString(index);
  }

  @Override
  protected String doConvertToLogFormat(String value) {
    return "'" + value + "'";
  }
}
