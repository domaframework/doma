package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Float} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class FloatType extends AbstractJdbcType<Float> {

  public FloatType() {
    super(Types.FLOAT);
  }

  @Override
  public Float doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getFloat(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Float value)
      throws SQLException {
    preparedStatement.setDouble(index, value);
  }

  @Override
  protected Float doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getFloat(index);
  }

  @Override
  protected String doConvertToLogFormat(Float value) {
    return String.valueOf(value);
  }
}
