package org.seasar.doma.jdbc.query;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

public class BlobCreateQuery extends AbstractCreateQuery<Blob> {

  @Override
  public Blob create(Connection connection) throws SQLException {
    return connection.createBlob();
  }
}
