package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#SMALLINT} and {@link Short}. */
public class ShortType extends AbstractJdbcType<Short> {

  public ShortType() {
    super(Types.SMALLINT);
  }

  @Override
  public Short doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getShort(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Short value)
      throws SQLException {
    preparedStatement.setShort(index, value);
  }

  @Override
  protected Short doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getShort(index);
  }

  @Override
  protected String doConvertToLogFormat(Short value) {
    return String.valueOf(value);
  }
}
