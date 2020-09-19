package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.command.CallableSqlParameterBinder;
import org.seasar.doma.internal.jdbc.command.CallableSqlParameterFetcher;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.ModuleQuery;

public abstract class ModuleCommand<QUERY extends ModuleQuery, RESULT> implements Command<RESULT> {

  protected final QUERY query;

  protected final CallableSql sql;

  protected ModuleCommand(QUERY query) {
    assertNotNull(query);
    this.query = query;
    this.sql = query.getSql();
  }

  @Override
  public QUERY getQuery() {
    return query;
  }

  @Override
  public RESULT execute() {
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      CallableStatement callableStatement = JdbcUtil.prepareCall(connection, sql);
      try {
        log();
        setupOptions(callableStatement);
        bindParameters(callableStatement);
        return executeInternal(callableStatement);
      } catch (SQLException e) {
        Dialect dialect = query.getConfig().getDialect();
        throw new SqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        JdbcUtil.close(callableStatement, query.getConfig().getJdbcLogger());
      }
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }

  protected abstract RESULT executeInternal(CallableStatement callableStatement)
      throws SQLException;

  protected void setupOptions(CallableStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected void bindParameters(CallableStatement callableStatement) throws SQLException {
    CallableSqlParameterBinder binder = new CallableSqlParameterBinder(query);
    binder.bind(callableStatement, sql.getParameters());
  }

  protected void fetchParameters(CallableStatement callableStatement) throws SQLException {
    CallableSqlParameterFetcher fetcher = new CallableSqlParameterFetcher(query);
    fetcher.fetch(callableStatement, sql.getParameters());
  }

  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }
}
