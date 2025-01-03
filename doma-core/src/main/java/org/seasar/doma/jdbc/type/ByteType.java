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

/** A JDBC type for {@link Types#SMALLINT} and {@link Byte}. */
public class ByteType extends AbstractJdbcType<Byte> {

  public ByteType() {
    super(Types.SMALLINT);
  }

  @Override
  public Byte doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getByte(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Byte value)
      throws SQLException {
    preparedStatement.setByte(index, value);
  }

  @Override
  protected Byte doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getByte(index);
  }

  @Override
  protected String doConvertToLogFormat(Byte value) {
    return String.valueOf(value);
  }
}
