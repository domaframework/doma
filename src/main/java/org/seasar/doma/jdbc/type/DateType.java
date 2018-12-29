package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#DATE} and {@link Date}. */
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
