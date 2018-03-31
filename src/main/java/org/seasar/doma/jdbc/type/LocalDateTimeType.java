package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

/** A JDBC type for {@link Types#TIMESTAMP} and {@link LocalDateTime}. */
public class LocalDateTimeType extends AbstractJdbcType<LocalDateTime> {

  public LocalDateTimeType() {
    super(Types.TIMESTAMP);
  }

  @Override
  public LocalDateTime doGetValue(ResultSet resultSet, int index) throws SQLException {
    var timestamp = resultSet.getTimestamp(index);
    return timestamp != null ? timestamp.toLocalDateTime() : null;
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, LocalDateTime value)
      throws SQLException {
    preparedStatement.setTimestamp(index, Timestamp.valueOf(value));
  }

  @Override
  protected LocalDateTime doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    var timestamp = callableStatement.getTimestamp(index);
    return timestamp != null ? timestamp.toLocalDateTime() : null;
  }

  @Override
  protected String doConvertToLogFormat(LocalDateTime value) {
    return "'" + Timestamp.valueOf(value) + "'";
  }
}
