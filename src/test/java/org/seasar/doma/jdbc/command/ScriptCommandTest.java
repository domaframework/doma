package org.seasar.doma.jdbc.command;

import java.sql.SQLException;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockStatement;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.StaticScriptQuery;

public class ScriptCommandTest extends TestCase {

  public void testExecute() throws Exception {
    var query = new StaticScriptQuery();
    query.setConfig(new MockConfig());
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(getName()));
    query.setBlockDelimiter("");
    query.prepare();
    query.setSqlLogType(SqlLogType.FORMATTED);
    var command = new ScriptCommand(query);
    command.execute();
  }

  public void testExecute_ScriptException() throws Exception {
    var config = new MockConfig();
    config.dataSource.connection.statement =
        new MockStatement() {

          @Override
          public boolean execute(String sql) throws SQLException {
            throw new SQLException("mock error.");
          }
        };

    var query = new StaticScriptQuery();
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(getName()));
    query.setBlockDelimiter("");
    query.setHaltOnError(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    var command = new ScriptCommand(query);
    try {
      command.execute();
      fail();
    } catch (ScriptException expected) {
      System.out.println(expected.getMessage());
    }
  }
}
