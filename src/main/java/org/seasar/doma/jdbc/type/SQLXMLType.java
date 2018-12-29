package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;

/**
 * {@link SQLXML} 用の {@link JdbcType} の実装です。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public class SQLXMLType extends AbstractJdbcType<SQLXML> {

  public SQLXMLType() {
    super(Types.SQLXML);
  }

  @Override
  protected SQLXML doGetValue(ResultSet resultSet, int index) throws SQLException {
    return resultSet.getSQLXML(index);
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, SQLXML value)
      throws SQLException {
    preparedStatement.setSQLXML(index, value);
  }

  @Override
  protected SQLXML doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    return callableStatement.getSQLXML(index);
  }

  @Override
  protected String doConvertToLogFormat(SQLXML value) {
    return value.toString();
  }
}
