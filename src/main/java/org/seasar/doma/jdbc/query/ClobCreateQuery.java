package org.seasar.doma.jdbc.query;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;

/** @author taedium */
public class ClobCreateQuery extends AbstractCreateQuery<Clob> {

  @Override
  public Clob create(Connection connection) throws SQLException {
    return connection.createClob();
  }
}
