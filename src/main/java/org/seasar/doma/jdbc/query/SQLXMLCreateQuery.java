package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public class SQLXMLCreateQuery extends AbstractCreateQuery<SQLXML> {

  @Override
  public SQLXML create(Connection connection) throws SQLException {
    return connection.createSQLXML();
  }
}
