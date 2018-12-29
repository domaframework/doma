package org.seasar.doma.jdbc.type;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link BigDecimal} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class BigDecimalType extends AbstractJdbcType<BigDecimal> {

  public BigDecimalType() {
    super(Types.DECIMAL);
  }

  @Override
  protected BigDecimal doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getBigDecimal(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, BigDecimal value)
      throws SQLException {
    preparedStatement.setBigDecimal(index, value);
  }

  @Override
  protected BigDecimal doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    return callableStatement.getBigDecimal(index);
  }

  @Override
  protected String doConvertToLogFormat(BigDecimal value) {
    return value.toPlainString();
  }
}
