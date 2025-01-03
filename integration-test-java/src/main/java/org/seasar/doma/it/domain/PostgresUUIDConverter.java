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
package org.seasar.doma.it.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.JdbcTypeProvider;
import org.seasar.doma.jdbc.type.AbstractJdbcType;
import org.seasar.doma.jdbc.type.JdbcType;

@ExternalDomain
public class PostgresUUIDConverter extends JdbcTypeProvider<UUID> {

  private static final PostgresUUIDJdbcType jdbcType = new PostgresUUIDJdbcType();

  @Override
  public JdbcType<UUID> getJdbcType() {
    return jdbcType;
  }
}

class PostgresUUIDJdbcType extends AbstractJdbcType<UUID> {

  protected PostgresUUIDJdbcType() {
    super(Types.OTHER);
  }

  @Override
  protected UUID doGetValue(ResultSet resultSet, int index) throws SQLException {
    String value = resultSet.getString(index);
    return value == null ? null : UUID.fromString(value);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, UUID value)
      throws SQLException {
    preparedStatement.setObject(index, value, type);
  }

  @Override
  protected UUID doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    String value = callableStatement.getString(index);
    return value == null ? null : UUID.fromString(value);
  }

  @Override
  protected String doConvertToLogFormat(UUID value) {
    return value.toString();
  }
}
