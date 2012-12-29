/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockStatement;
import org.seasar.doma.internal.jdbc.query.SqlFileScriptQuery;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.jdbc.ScriptException;

/**
 * @author taedium
 * 
 */
public class ScriptCommandTest extends TestCase {

    public void testExecute() throws Exception {
        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(new MockConfig());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setScriptFilePath(ScriptFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setBlockDelimiter("");
        query.prepare();
        ScriptCommand command = new ScriptCommand(query);
        command.execute();
    }

    public void testExecute_ScriptException() throws Exception {
        MockConfig config = new MockConfig();
        config.dataSource.connection.statement = new MockStatement() {

            @Override
            public boolean execute(String sql) throws SQLException {
                throw new SQLException("mock error.");
            }

        };

        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setScriptFilePath(ScriptFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setBlockDelimiter("");
        query.setHaltOnError(true);
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
