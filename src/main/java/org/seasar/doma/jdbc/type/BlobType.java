package org.seasar.doma.jdbc.type;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Blob} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class BlobType extends AbstractJdbcType<Blob> {

  public BlobType() {
    super(Types.BLOB);
  }

  @Override
  protected Blob doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getBlob(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Blob value)
      throws SQLException {
    preparedStatement.setBlob(index, value);
  }

  @Override
  protected Blob doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getBlob(index);
  }

  @Override
  protected String doConvertToLogFormat(Blob value) {
    return value.toString();
  }
}
