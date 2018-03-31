package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/** A JDBC type for {@link Types#DOUBLE} and {@link Double}. */
public class DoubleType extends AbstractJdbcType<Double> {

  public DoubleType() {
    super(Types.DOUBLE);
  }

  @Override
  public Double doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getDouble(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Double value)
      throws SQLException {
    preparedStatement.setDouble(index, value);
  }

  @Override
  protected Double doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getDouble(index);
  }

  @Override
  protected String doConvertToLogFormat(Double value) {
    return String.valueOf(value);
  }
}
