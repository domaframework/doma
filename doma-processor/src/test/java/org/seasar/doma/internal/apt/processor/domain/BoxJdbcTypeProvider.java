package org.seasar.doma.internal.apt.processor.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.JdbcTypeProvider;
import org.seasar.doma.jdbc.type.AbstractJdbcType;
import org.seasar.doma.jdbc.type.JdbcType;

@ExternalDomain
public class BoxJdbcTypeProvider extends JdbcTypeProvider<Box> {

  @Override
  public JdbcType<Box> getJdbcType() {
    return new AbstractJdbcType<>(Types.OTHER) {

      @Override
      protected Box doGetValue(ResultSet resultSet, int index) throws SQLException {
        return null;
      }

      @Override
      protected void doSetValue(PreparedStatement preparedStatement, int index, Box value)
          throws SQLException {}

      @Override
      protected Box doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        return null;
      }

      @Override
      protected String doConvertToLogFormat(Box value) {
        return "";
      }
    };
  }
}
