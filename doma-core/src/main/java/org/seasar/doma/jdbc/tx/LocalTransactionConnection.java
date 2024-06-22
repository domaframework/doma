package org.seasar.doma.jdbc.tx;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * A connection for local transactions.
 *
 * <p>Ignores the invocation of the {@code close()} method.
 *
 * @see LocalTransaction
 */
class LocalTransactionConnection implements Connection {

  private final Connection connection;

  private final Integer preservedTransactionIsolation;

  private final boolean preservedAutoCommitState;

  /**
   * Creates an instance.
   *
   * @param connection the internal connection
   * @param preservedTransactionIsolation the transaction isolation to be preserved
   * @param preservedAutoCommitState the auto commit state to be preserved
   */
  public LocalTransactionConnection(
      Connection connection,
      Integer preservedTransactionIsolation,
      boolean preservedAutoCommitState) {
    assertNotNull(connection);
    assertTrue(!(connection instanceof LocalTransactionConnection));
    this.connection = connection;
    this.preservedTransactionIsolation = preservedTransactionIsolation;
    this.preservedAutoCommitState = preservedAutoCommitState;
  }

  protected Integer getPreservedTransactionIsolation() {
    return this.preservedTransactionIsolation;
  }

  protected boolean getPreservedAutoCommitState() {
    return preservedAutoCommitState;
  }

  /**
   * Returns the wrapped internal connection.
   *
   * @return the internal connection
   */
  public Connection getWrappedConnection() {
    return connection;
  }

  @Override
  public void clearWarnings() throws SQLException {
    connection.clearWarnings();
  }

  @Override
  public void close() {
    // do nothing.
  }

  @Override
  public void commit() throws SQLException {
    connection.commit();
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return connection.createArrayOf(typeName, elements);
  }

  @Override
  public Blob createBlob() throws SQLException {
    return connection.createBlob();
  }

  @Override
  public Clob createClob() throws SQLException {
    return connection.createClob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return connection.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return connection.createSQLXML();
  }

  @Override
  public Statement createStatement() throws SQLException {
    return connection.createStatement();
  }

  @Override
  public Statement createStatement(
      int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency);
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return connection.createStruct(typeName, attributes);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return connection.getAutoCommit();
  }

  @Override
  public String getCatalog() throws SQLException {
    return connection.getCatalog();
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return connection.getClientInfo();
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    return connection.getClientInfo(name);
  }

  @Override
  public int getHoldability() throws SQLException {
    return connection.getHoldability();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return connection.getMetaData();
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return connection.getTransactionIsolation();
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return connection.getTypeMap();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return connection.getWarnings();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return connection.isClosed();
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return connection.isReadOnly();
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return connection.isValid(timeout);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    if (iface == null) {
      return false;
    }
    if (iface.isAssignableFrom(getClass())) {
      return true;
    }
    return connection.isWrapperFor(iface);
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    return connection.nativeSQL(sql);
  }

  @Override
  public CallableStatement prepareCall(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    return connection.prepareCall(sql);
  }

  @Override
  public PreparedStatement prepareStatement(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    return connection.prepareStatement(
        sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return connection.prepareStatement(sql, autoGeneratedKeys);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return connection.prepareStatement(sql, columnIndexes);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return connection.prepareStatement(sql, columnNames);
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    connection.releaseSavepoint(savepoint);
  }

  @Override
  public void rollback() throws SQLException {
    connection.rollback();
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    connection.rollback(savepoint);
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    connection.setAutoCommit(autoCommit);
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    connection.setCatalog(catalog);
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    connection.setClientInfo(properties);
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    connection.setClientInfo(name, value);
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    connection.setHoldability(holdability);
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    connection.setReadOnly(readOnly);
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return connection.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    return connection.setSavepoint(name);
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    connection.setTransactionIsolation(level);
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    connection.setTypeMap(map);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface == null) {
      throw new SQLException("iface must not be null");
    }
    if (iface.isAssignableFrom(getClass())) {
      return (T) this;
    }
    return connection.unwrap(iface);
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    connection.setSchema(schema);
  }

  @Override
  public String getSchema() throws SQLException {
    return connection.getSchema();
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    connection.abort(executor);
  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    connection.setNetworkTimeout(executor, milliseconds);
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return connection.getNetworkTimeout();
  }
}
