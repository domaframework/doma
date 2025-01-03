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
import java.time.LocalDateTime;

/** A JDBC type for {@link Types#TIMESTAMP} and {@link LocalDateTime}. */
public class LocalDateTimeType extends AbstractJdbcType<LocalDateTime> {

  public LocalDateTimeType() {
    super(Types.TIMESTAMP);
  }

  @Override
  public LocalDateTime doGetValue(ResultSet resultSet, int index) throws SQLException {
    Timestamp timestamp = resultSet.getTimestamp(index);
    return timestamp != null ? timestamp.toLocalDateTime() : null;
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, LocalDateTime value)
      throws SQLException {
    preparedStatement.setTimestamp(index, Timestamp.valueOf(value));
  }

  @Override
  protected LocalDateTime doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    Timestamp timestamp = callableStatement.getTimestamp(index);
    return timestamp != null ? timestamp.toLocalDateTime() : null;
  }

  @Override
  protected String doConvertToLogFormat(LocalDateTime value) {
    return "'" + Timestamp.valueOf(value) + "'";
  }
}
