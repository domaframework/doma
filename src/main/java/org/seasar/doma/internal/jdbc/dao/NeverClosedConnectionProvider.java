package org.seasar.doma.internal.jdbc.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/** @author taedium */
class NeverClosedConnectionProvider implements DataSource {

  private final NeverClosedConnection connection;

  public NeverClosedConnectionProvider(NeverClosedConnection connection) {
    assertNotNull(connection);
    this.connection = connection;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return connection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return connection;
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {}

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {}

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface != null && iface.isAssignableFrom(getClass());
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
    throw new SQLException("cannot unwrap to " + iface.getName());
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }
}
