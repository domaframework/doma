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
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * A JDBC type for {@link Types#VARCHAR} and {@link Enum}.
 *
 * @param <E> the enum subclass
 */
public class EnumType<E extends Enum<E>> extends AbstractJdbcType<E> {

  protected final Class<E> enumClass;

  /**
   * Creates an instance.
   *
   * @param enumClass the {@link Enum} class
   * @throws DomaNullPointerException thrown, if {@code enumClass} is {@code null}
   */
  public EnumType(Class<E> enumClass) {
    super(Types.VARCHAR);
    if (enumClass == null) {
      throw new DomaNullPointerException("enumClass");
    }
    this.enumClass = enumClass;
  }

  @Override
  protected E doGetValue(ResultSet resultSet, int index) throws SQLException {
    String value = resultSet.getString(index);
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, value);
    } catch (IllegalArgumentException e) {
      throw new JdbcException(Message.DOMA2040, enumClass.getName(), value);
    }
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, E value)
      throws SQLException {
    preparedStatement.setString(index, value.name());
  }

  @Override
  protected E doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    String value = callableStatement.getString(index);
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, value);
    } catch (IllegalArgumentException e) {
      throw new JdbcException(Message.DOMA2040, enumClass.getName(), value);
    }
  }

  @Override
  protected String doConvertToLogFormat(E value) {
    return "'" + value.name() + "'";
  }
}
