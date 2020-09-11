package org.seasar.doma.internal.jdbc.mock;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class MockDataSource extends MockWrapper implements DataSource {

  public MockConnection connection = new MockConnection();

  public MockDataSource() {}

  public MockDataSource(MockConnection connection) {
    this.connection = connection;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return connection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new AssertionError();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new AssertionError();
  }

  @SuppressWarnings("all")
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new AssertionError();
  }
}
