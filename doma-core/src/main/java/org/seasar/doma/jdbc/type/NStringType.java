package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#NVARCHAR} and {@link String}. */
public class NStringType extends AbstractJdbcType<String> {

  public NStringType() {
    super(Types.NVARCHAR);
  }

  @Override
  protected String doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getNString(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, String value)
      throws SQLException {
    preparedStatement.setNString(index, value);
  }

  @Override
  protected String doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getNString(index);
  }

  @Override
  protected String doConvertToLogFormat(String value) {
    return "'" + value + "'";
  }
}
