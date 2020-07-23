package org.seasar.doma.jdbc.type;

import java.sql.*;

/** A JDBC type for {@link Types#OTHER} and {@link Object}. */
public class ObjectType extends AbstractJdbcType<Object> {

  public ObjectType() {
    super(Types.OTHER);
  }

  @Override
  protected Object doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getObject(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Object value)
      throws SQLException {
    preparedStatement.setObject(index, value);
  }

  @Override
  protected Object doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getObject(index);
  }

  @Override
  protected String doConvertToLogFormat(Object value) {
    return "'" + value + "'";
  }
}
