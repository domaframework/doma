package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#SMALLINT} and {@link Byte}. */
public class ByteType extends AbstractJdbcType<Byte> {

  public ByteType() {
    super(Types.SMALLINT);
  }

  @Override
  public Byte doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getByte(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Byte value)
      throws SQLException {
    preparedStatement.setByte(index, value);
  }

  @Override
  protected Byte doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getByte(index);
  }

  @Override
  protected String doConvertToLogFormat(Byte value) {
    return String.valueOf(value);
  }
}
