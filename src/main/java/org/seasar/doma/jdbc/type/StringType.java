package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#VARCHAR} and {@link String}. */
public class StringType extends AbstractJdbcType<String> {

  public StringType() {
    super(Types.VARCHAR);
  }

  @Override
  protected String doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getString(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, String value)
      throws SQLException {
    preparedStatement.setString(index, value);
  }

  @Override
  protected String doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getString(index);
  }

  @Override
  protected String doConvertToLogFormat(String value) {
    return "'" + value + "'";
  }
}
