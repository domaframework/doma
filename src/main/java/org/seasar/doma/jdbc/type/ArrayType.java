package org.seasar.doma.jdbc.type;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Array} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class ArrayType extends AbstractJdbcType<Array> {

  public ArrayType() {
    super(Types.ARRAY);
  }

  @Override
  protected Array doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getArray(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Array value)
      throws SQLException {
    preparedStatement.setArray(index, value);
  }

  @Override
  protected Array doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getArray(index);
  }

  @Override
  protected String doConvertToLogFormat(Array value) {
    return "'" + value + "'";
  }
}
