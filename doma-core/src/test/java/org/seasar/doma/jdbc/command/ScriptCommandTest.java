/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
