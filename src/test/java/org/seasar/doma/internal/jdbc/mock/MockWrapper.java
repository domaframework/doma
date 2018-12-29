package org.seasar.doma.internal.jdbc.mock;

import java.sql.SQLException;
import java.sql.Wrapper;

/** @author taedium */
public class MockWrapper implements Wrapper {

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
}
