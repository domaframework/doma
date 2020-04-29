package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.ScriptReader;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.AbstractSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.query.ScriptQuery;

public class ScriptCommand implements Command<Void> {

  protected final ScriptQuery query;

  protected ScriptException savedScriptException;

  public ScriptCommand(ScriptQuery query) {
    assertNotNull(query);
    this.query = query;
  }

  @Override
  public ScriptQuery getQuery() {
    return query;
  }

  @Override
  public Void execute() {
    Config config = query.getConfig();
    Connection connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      ScriptReader reader = new ScriptReader(query);
      try {
        for (String script = reader.readSql(); script != null; script = reader.readSql()) {
          ScriptSql sql =
              new ScriptSql(
                  script, query.getScriptFilePath(), query.getSqlLogType(), query::comment);
          Statement statement = JdbcUtil.createStatement(connection);
          try {
            log(sql);
            setupOptions(statement);
            statement.execute(script);
          } catch (Exception e) {
            if (query.getHaltOnError()) {
              throw new ScriptException(e, sql, reader.getLineNumber());
            }
            if (savedScriptException == null) {
              savedScriptException = new ScriptException(e, sql, reader.getLineNumber());
            }
          } finally {
            JdbcUtil.close(statement, config.getJdbcLogger());
          }
        }
      } finally {
        reader.close();
      }
    } finally {
      JdbcUtil.close(connection, config.getJdbcLogger());
    }
    throwSavedScriptExceptionIfExists();
    return null;
  }

  protected void log(ScriptSql sql) {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  protected void setupOptions(Statement statement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      statement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected void throwSavedScriptExceptionIfExists() {
    if (savedScriptException != null) {
      throw savedScriptException;
    }
  }

  protected static class ScriptSql extends AbstractSql<SqlParameter> {

    public ScriptSql(
        String rawSql,
        String sqlFilePath,
        SqlLogType sqlLogType,
        Function<String, String> converter) {
      super(
          SqlKind.SCRIPT,
          rawSql,
          rawSql,
          sqlFilePath,
          Collections.emptyList(),
          sqlLogType,
          converter);
    }
  }
}
