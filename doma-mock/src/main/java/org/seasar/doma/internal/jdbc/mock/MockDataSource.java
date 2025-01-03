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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

@SuppressWarnings("RedundantThrows")
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
