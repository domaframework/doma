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
package org.seasar.doma.internal.apt.processor.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.JdbcTypeProvider;
import org.seasar.doma.jdbc.type.AbstractJdbcType;
import org.seasar.doma.jdbc.type.JdbcType;

@ExternalDomain
public class BoxJdbcTypeProvider extends JdbcTypeProvider<Box> {

  @Override
  public JdbcType<Box> getJdbcType() {
    return new AbstractJdbcType<>(Types.OTHER) {

      @Override
      protected Box doGetValue(ResultSet resultSet, int index) throws SQLException {
        return null;
      }

      @Override
      protected void doSetValue(PreparedStatement preparedStatement, int index, Box value)
          throws SQLException {}

      @Override
      protected Box doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        return null;
      }

      @Override
      protected String doConvertToLogFormat(Box value) {
        return "";
      }
    };
  }
}
