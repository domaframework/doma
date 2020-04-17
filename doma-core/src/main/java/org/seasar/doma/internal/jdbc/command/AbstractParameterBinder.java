package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractParameterBinder<
        STATEMENT extends PreparedStatement, PARAMETER extends SqlParameter>
    implements ParameterBinder<STATEMENT, PARAMETER> {

  protected <BASIC> void bindInParameter(
      STATEMENT statement,
      JdbcMappable<BASIC> parameter,
      int index,
      JdbcMappingVisitor jdbcMappingVisitor)
      throws SQLException {
    Wrapper<?> wrapper = parameter.getWrapper();
    wrapper.accept(jdbcMappingVisitor, new JdbcValueSetter(statement, index), parameter);
  }
}
