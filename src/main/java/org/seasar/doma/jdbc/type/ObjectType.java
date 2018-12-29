package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Object} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class ObjectType extends AbstractJdbcType<Object> {

  public ObjectType() {
    super(Types.OTHER);
  }

  @Override
  protected Object doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getObject(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Object value)
      throws SQLException {
    preparedStatement.setObject(index, value);
  }

  @Override
  protected Object doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getObject(index);
  }

  @Override
  protected String doConvertToLogFormat(Object value) {
    return "'" + value + "'";
  }
}
