package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link Date} 用の {@link JdbcType} の実装です。
 *
 * @author taedium
 * @since 1.9.0
 */
public class UtilDateType extends AbstractJdbcType<Date> {

  public UtilDateType() {
    super(Types.TIMESTAMP);
  }

  @Override
  public Date doGetValue(ResultSet resultSet, int index) throws SQLException {
    Timestamp timestamp = resultSet.getTimestamp(index);
    if (timestamp == null) {
      return null;
    }
    return new Date(timestamp.getTime());
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, Date value)
      throws SQLException {
    preparedStatement.setTimestamp(index, new Timestamp(value.getTime()));
  }

  @Override
  protected Date doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    Timestamp timestamp = callableStatement.getTimestamp(index);
    if (timestamp == null) {
      return null;
    }
    return new Date(timestamp.getTime());
  }

  @Override
  protected String doConvertToLogFormat(Date value) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return "'" + dateFormat.format(value) + "'";
  }
}
