package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#NCLOB} and {@link NClob}. */
public class NClobType extends AbstractJdbcType<NClob> {

  public NClobType() {
    super(Types.NCLOB);
  }

  @Override
  protected NClob doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getNClob(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, NClob value)
      throws SQLException {
    preparedStatement.setNClob(index, value);
  }

  @Override
  protected NClob doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getNClob(index);
  }

  @Override
  protected String doConvertToLogFormat(NClob value) {
    return String.valueOf(value);
  }
}
