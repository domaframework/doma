package org.seasar.doma.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import junit.framework.TestCase;

public class SimpleDataSourceTest extends TestCase {

  public void testUrlIsNull() throws Exception {
    var dataSource = new SimpleDataSource();
    dataSource.setUser("user");
    dataSource.setPassword("password");
    try {
      dataSource.getConnection();
      fail();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void testNoSuitableDriverFound() throws Exception {
    var dataSource = new SimpleDataSource();
    dataSource.setUser("user");
    dataSource.setPassword("password");
    dataSource.setUrl("url");
    try {
      dataSource.getConnection();
      fail();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void testIsWrapperFor() throws Exception {
    DataSource dataSource = new SimpleDataSource();
    assertTrue(dataSource.isWrapperFor(SimpleDataSource.class));
    assertFalse(dataSource.isWrapperFor(Runnable.class));
  }

  public void testUnwrap() throws Exception {
    DataSource dataSource = new SimpleDataSource();
    assertNotNull(dataSource.unwrap(SimpleDataSource.class));
    try {
      dataSource.unwrap(Runnable.class);
      fail();
    } catch (SQLException ignored) {
    }
  }
}
