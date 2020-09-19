package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

public class SimpleDataSourceTest {

  @Test
  public void testUrlIsNull() {
    SimpleDataSource dataSource = new SimpleDataSource();
    dataSource.setUser("user");
    dataSource.setPassword("password");
    try {
      dataSource.getConnection();
      fail();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNoSuitableDriverFound() {
    SimpleDataSource dataSource = new SimpleDataSource();
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

  @Test
  public void testIsWrapperFor() throws Exception {
    DataSource dataSource = new SimpleDataSource();
    assertTrue(dataSource.isWrapperFor(SimpleDataSource.class));
    assertFalse(dataSource.isWrapperFor(Runnable.class));
  }

  @Test
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
