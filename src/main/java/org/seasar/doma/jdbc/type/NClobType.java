package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link NClob} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 */
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
