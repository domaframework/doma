package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Boolean} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class BooleanType extends AbstractJdbcType<Boolean> {

  public BooleanType() {
    super(Types.BOOLEAN);
  }

  @Override
  protected Boolean doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getBoolean(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Boolean value)
      throws SQLException {
    preparedStatement.setBoolean(index, value);
  }

  @Override
  protected Boolean doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getBoolean(index);
  }

  @Override
  protected String doConvertToLogFormat(Boolean value) {
    return "'" + value + "'";
  }
}
