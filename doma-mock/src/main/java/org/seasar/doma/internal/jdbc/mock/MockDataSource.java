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
    AssertionUtil.notYetImplemented();
    return null;
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    AssertionUtil.notYetImplemented();
    return 0;
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    AssertionUtil.notYetImplemented();
    return null;
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    AssertionUtil.notYetImplemented();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    AssertionUtil.notYetImplemented();
  }

  @SuppressWarnings("all")
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    AssertionUtil.notYetImplemented();
    return null;
  }
}
