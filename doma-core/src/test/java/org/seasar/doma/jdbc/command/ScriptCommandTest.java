package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockStatement;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlFileScriptQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ScriptCommandTest {

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute() {
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(new MockConfig());
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(ScriptFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setBlockDelimiter("");
    query.prepare();
    query.setSqlLogType(SqlLogType.FORMATTED);
    ScriptCommand command = new ScriptCommand(query);
    command.execute();
  }

  @Test
  public void testExecute_ScriptException() {
    MockConfig config = new MockConfig();
    config.dataSource.connection.statement =
        new MockStatement() {

          @Override
          public boolean execute(String sql) throws SQLException {
            throw new SQLException("mock error.");
          }
        };

    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(ScriptFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setBlockDelimiter("");
    query.setHaltOnError(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    ScriptCommand command = new ScriptCommand(query);
    try {
      command.execute();
      fail();
    } catch (ScriptException expected) {
      System.out.println(expected.getMessage());
    }
  }
}
