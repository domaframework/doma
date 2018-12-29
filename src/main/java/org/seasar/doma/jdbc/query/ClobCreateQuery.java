package org.seasar.doma.jdbc.query;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;

public class ClobCreateQuery extends AbstractCreateQuery<Clob> {

  @Override
  public Clob create(Connection connection) throws SQLException {
    return connection.createClob();
  }
}
