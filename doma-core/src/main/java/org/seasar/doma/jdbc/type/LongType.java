package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#BIGINT} and {@link Long}. */
public class LongType extends AbstractJdbcType<Long> {

  public LongType() {
    super(Types.BIGINT);
  }

  @Override
  protected Long doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getLong(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Long value)
      throws SQLException {
    preparedStatement.setLong(index, value);
  }

  @Override
  protected Long doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getLong(index);
  }

  @Override
  protected String doConvertToLogFormat(Long value) {
    return String.valueOf(value);
  }
}
