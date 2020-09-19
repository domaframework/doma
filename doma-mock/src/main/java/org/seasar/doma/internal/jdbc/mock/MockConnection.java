package org.seasar.doma.internal.jdbc.mock;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MockConnection extends MockWrapper implements Connection {

  public MockStatement statement = new MockStatement();

  public MockPreparedStatement preparedStatement = new MockPreparedStatement();

  public MockCallableStatement callableStatement = new MockCallableStatement();

  public boolean closed;

  public boolean committed;

  public boolean rolledback;

  public boolean autoCommit = true;

  public final List<String> savepointNames = new ArrayList<>();

  public int isolationLevel = Connection.TRANSACTION_READ_COMMITTED;

  public MockConnection() {}

  public MockConnection(MockPreparedStatement preparedStatement) {
    this.preparedStatement = preparedStatement;
  }

  public MockConnection(MockCallableStatement callableStatement) {
    this.callableStatement = callableStatement;
    this.preparedStatement = callableStatement;
  }

  @Override
  public void clearWarnings() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void close() throws SQLException {
    closed = true;
  }

  @Override
  public void commit() throws SQLException {
    this.committed = true;
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Blob createBlob() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Clob createClob() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public NClob createNClob() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Statement createStatement() throws SQLException {
    return statement;
  }

  @Override
  public Statement createStatement(
      int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return autoCommit;
  }

  @Override
  public String getCatalog() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getHoldability() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return isolationLevel;
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return closed;
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public CallableStatement prepareCall(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    callableStatement.sql = sql;
    return callableStatement;
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    callableStatement.sql = sql;
    return callableStatement;
  }

  @Override
  public PreparedStatement prepareStatement(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    AssertionUtil.assertTrue(!closed);
    preparedStatement.sql = sql;
    return preparedStatement;
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    String savepointName = savepoint.getSavepointName();
    int pos = savepointNames.lastIndexOf(savepointName);
    if (pos == -1) {
      throw new SQLException();
    }
    savepointNames.subList(0, pos + 1).clear();
  }

  @Override
  public void rollback() throws SQLException {
    this.rolledback = true;
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    String name = savepoint.getSavepointName();
    savepointNames.remove(name);
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    this.autoCommit = autoCommit;
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    throw new AssertionError();
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    throw new AssertionError();
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public Savepoint setSavepoint(final String name) throws SQLException {
    savepointNames.add(name);
    return new Savepoint() {

      @Override
      public String getSavepointName() throws SQLException {
        return name;
      }

      @Override
      public int getSavepointId() throws SQLException {
        throw new SQLException();
      }
    };
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    isolationLevel = level;
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void setSchema(String schema) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public String getSchema() throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void abort(Executor executor) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public int getNetworkTimeout() throws SQLException {
    throw new AssertionError();
  }
}
