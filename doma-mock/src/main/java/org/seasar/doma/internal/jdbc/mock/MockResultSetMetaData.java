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
package org.seasar.doma.internal.jdbc.mock;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("RedundantThrows")
public class MockResultSetMetaData extends MockWrapper implements ResultSetMetaData {

  public final List<ColumnMetaData> columns = new ArrayList<>();

  @Override
  public String getCatalogName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getColumnClassName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getColumnCount() throws SQLException {
    return columns.size();
  }

  @Override
  public int getColumnDisplaySize(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getColumnLabel(int column) throws SQLException {
    return columns.get(column - 1).getLabel();
  }

  @Override
  public String getColumnName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getColumnType(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getColumnTypeName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getPrecision(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getScale(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getSchemaName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getTableName(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isAutoIncrement(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isCaseSensitive(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isCurrency(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int isNullable(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isReadOnly(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isSearchable(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isSigned(int column) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isWritable(int column) throws SQLException {
    throw new AssertionError();
  }
}
