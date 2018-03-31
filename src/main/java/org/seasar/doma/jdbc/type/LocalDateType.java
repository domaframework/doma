package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/** A JDBC type for {@link Types#DATE} and {@link LocalDate}. */
public class LocalDateType extends AbstractJdbcType<LocalDate> {

  public LocalDateType() {
    super(Types.DATE);
  }

  @Override
  public LocalDate doGetValue(ResultSet resultSet, int index) throws SQLException {
    var date = resultSet.getDate(index);
    return date != null ? date.toLocalDate() : null;
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, LocalDate value)
      throws SQLException {
    preparedStatement.setDate(index, Date.valueOf(value));
  }

  @Override
  protected LocalDate doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    var date = callableStatement.getDate(index);
    return date != null ? date.toLocalDate() : null;
  }

  @Override
  protected String doConvertToLogFormat(LocalDate value) {
    return "'" + Date.valueOf(value) + "'";
  }
}
