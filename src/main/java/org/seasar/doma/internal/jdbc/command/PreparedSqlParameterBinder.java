package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.query.Query;

public class PreparedSqlParameterBinder
    extends AbstractParameterBinder<PreparedStatement, InParameter<?>> {

  protected final Query query;

  public PreparedSqlParameterBinder(Query query) {
    assertNotNull(query);
    this.query = query;
  }

  @Override
  public void bind(PreparedStatement preparedStatement, List<? extends InParameter<?>> parameters)
      throws SQLException {
    assertNotNull(preparedStatement, parameters);
    int index = 1;
    JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
    for (InParameter<?> parameter : parameters) {
      bindInParameter(preparedStatement, parameter, index, jdbcMappingVisitor);
      index++;
    }
  }
}
