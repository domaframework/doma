package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalTimeType extends AbstractJdbcType<LocalTime> {

  public LocalTimeType() {
    super(Types.TIME);
  }

  @Override
  public LocalTime doGetValue(ResultSet resultSet, int index) throws SQLException {
    Time time = resultSet.getTime(index);
    return time != null ? time.toLocalTime() : null;
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, LocalTime value)
      throws SQLException {
    preparedStatement.setTime(index, Time.valueOf(value));
  }

  @Override
  protected LocalTime doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    Time time = callableStatement.getTime(index);
    return time != null ? time.toLocalTime() : null;
  }

  @Override
  protected String doConvertToLogFormat(LocalTime value) {
    return "'" + Time.valueOf(value) + "'";
  }
}
