package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#TIME} and {@link Time}. */
public class TimeType extends AbstractJdbcType<Time> {

  public TimeType() {
    super(Types.TIME);
  }

  @Override
  protected Time doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getTime(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Time value)
      throws SQLException {
    preparedStatement.setTime(index, value);
  }

  @Override
  protected Time doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getTime(index);
  }

  @Override
  protected String doConvertToLogFormat(Time value) {
    return "'" + value + "'";
  }
}
