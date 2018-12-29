package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link Date} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
public class DateType extends AbstractJdbcType<Date> {

  public DateType() {
    super(Types.DATE);
  }

  @Override
  public Date doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getDate(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Date value)
      throws SQLException {
    preparedStatement.setDate(index, value);
  }

  @Override
  protected Date doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getDate(index);
  }

  @Override
  protected String doConvertToLogFormat(Date value) {
    return "'" + value + "'";
  }
}
