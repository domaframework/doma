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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings({"all", "deprecation"})
public class MockPreparedStatement extends MockStatement implements PreparedStatement {

  public MockResultSet resultSet = new MockResultSet();

  public List<BindValue> bindValues = new ArrayList<BindValue>();

  public String sql;

  public MockPreparedStatement() {}

  public MockPreparedStatement(MockResultSet resultSet) {
    this.resultSet = resultSet;
  }

  @Override
  public void addBatch() throws SQLException {
    addBatchCount++;
  }

  @Override
  public void clearParameters() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean execute() throws SQLException {
    return false;
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    AssertionUtil.assertTrue(!closed);
    if (resultSet == null) {
      resultSet = new MockResultSet();
    }
    return resultSet;
  }

  @Override
  public int executeUpdate() throws SQLException {
    AssertionUtil.assertTrue(!closed);
    return updatedRows;
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    bindValues.add(new BindValue("BigDecimal", parameterIndex, x));
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    bindValues.add(new BindValue("Int", parameterIndex, x));
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    bindValues.add(new BindValue("Long", parameterIndex, x));
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    bindValues.add(new BindValue(sqlType, parameterIndex));
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    bindValues.add(new BindValue("String", parameterIndex, x));
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void closeOnCompletion() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public boolean isCloseOnCompletion() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long getLargeUpdateCount() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void setLargeMaxRows(long max) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long getLargeMaxRows() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long[] executeLargeBatch() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long executeLargeUpdate(String sql) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)
      throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public long executeLargeUpdate() throws SQLException {
    throw new AssertionError();
  }
}
