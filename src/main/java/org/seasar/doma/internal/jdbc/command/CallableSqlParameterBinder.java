package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.jdbc.command.JdbcOutParameterRegistrar;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class CallableSqlParameterBinder
    extends AbstractParameterBinder<CallableStatement, SqlParameter> {

  protected final Query query;

  public CallableSqlParameterBinder(Query query) {
    assertNotNull(query);
    this.query = query;
  }

  @Override
  public void bind(CallableStatement callableStatement, List<? extends SqlParameter> parameters)
      throws SQLException {
    assertNotNull(callableStatement, parameters);
    BindingVisitor visitor = new BindingVisitor(query, callableStatement);
    for (SqlParameter parameter : parameters) {
      parameter.accept(visitor, null);
    }
  }

  protected class BindingVisitor implements SqlParameterVisitor<Void, SQLException> {

    protected final Dialect dialect;

    protected final JdbcMappingVisitor jdbcMappingVisitor;

    protected final CallableStatement callableStatement;

    protected int index = 1;

    public BindingVisitor(Query query, CallableStatement callableStatement) {
      this.dialect = query.getConfig().getDialect();
      this.jdbcMappingVisitor = dialect.getJdbcMappingVisitor();
      this.callableStatement = callableStatement;
    }

    @Override
    public <BASIC> void visitInParameter(InParameter<BASIC> parameter, Void p) throws SQLException {
      bindInParameter(parameter);
      index++;
    }

    @Override
    public <BASIC> void visitOutParameter(OutParameter<BASIC> parameter, Void p)
        throws SQLException {
      registerOutParameter(parameter);
      index++;
    }

    @Override
    public <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> void visitInOutParameter(
        INOUT parameter, Void p) throws SQLException {
      bindInParameter(parameter);
      registerOutParameter(parameter);
      index++;
    }

    @Override
    public <ELEMENT> void visitListParameter(ListParameter<ELEMENT> parameter, Void p)
        throws SQLException {
      registerListParameter(parameter);
    }

    @Override
    public <BASIC, RESULT> void visitSingleResultParameter(
        SingleResultParameter<BASIC, RESULT> parameter, Void p) throws SQLException {
      registerOutParameter(parameter);
      index++;
    }

    @Override
    public <ELEMENT> void visitResultListParameter(ResultListParameter<ELEMENT> parameter, Void p)
        throws SQLException {
      registerListParameter(parameter);
    }

    protected <BASIC> void bindInParameter(InParameter<BASIC> parameter) throws SQLException {
      CallableSqlParameterBinder.this.bindInParameter(
          callableStatement, parameter, index, jdbcMappingVisitor);
    }

    protected <BASIC> void registerOutParameter(JdbcMappable<BASIC> parameter) throws SQLException {
      Wrapper<BASIC> wrapper = parameter.getWrapper();
      wrapper.accept(
          jdbcMappingVisitor, new JdbcOutParameterRegistrar(callableStatement, index), parameter);
    }

    protected <ELEMENT> void registerListParameter(ListParameter<ELEMENT> parameter)
        throws SQLException {
      if (dialect.supportsResultSetReturningAsOutParameter()) {
        JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
        resultSetType.registerOutParameter(callableStatement, index);
        index++;
      }
    }
  }
}
