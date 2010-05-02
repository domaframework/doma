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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.SqlFileScriptQuery;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

/**
 * @author taedium
 * 
 */
public class ScriptReaderTest extends TestCase {

    private SqlFileScriptQuery query;

    @Override
    public void setUp() {
        MockConfig config = new MockConfig();
        config.dialect = new Mssql2008Dialect();
        query = new SqlFileScriptQuery() {

            @Override
            public void prepare() {
            }
        };
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setBlockDelimiter(config.dialect.getScriptBlockDelimiter());
        query.prepare();
    }

    public void testReadSql_delimiter() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("aaa;\n");
                buf.append("bbb\n");
                buf.append("go\n");
                buf.append("ccc\n");
                buf.append("ddd\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("aaa", reader.readSql());
        assertEquals("bbb", reader.readSql());
        assertEquals("ccc ddd", reader.readSql());
        assertNull(reader.readSql());
    }

    public void testReadSql_delimiterInLine() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("aaa; bbb; ccc;\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("aaa", reader.readSql());
        assertEquals("bbb", reader.readSql());
        assertEquals("ccc", reader.readSql());
        assertNull(reader.readSql());
    }

    public void testReadSql_sqlBlock() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("begin aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("begin aaa; end", reader.readSql());
        assertNull(reader.readSql());
    }

    public void testReadSql_sqlBlock_createTrigger() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("create trigger hoge begin aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("create trigger hoge begin aaa; end", reader.readSql());
        assertNull(reader.readSql());
    }

    public void testReadSql_notSqlBlock() throws Exception {
        ScriptReader reader = new ScriptReader(query) {
            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("start aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("start aaa", reader.readSql());
        assertEquals("end", reader.readSql());
    }

    public void testReadSql_commentBlock() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("select 1 ; /* aaa\n");
                buf.append("aaa */ select 2;");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("select 1", reader.readSql());
        assertEquals("select 2", reader.readSql());
        assertNull(reader.readSql());
    }

    public void testReadSql_lineNumber() throws Exception {
        ScriptReader reader = new ScriptReader(query) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("/*\n");
                buf.append(" *\n");
                buf.append(" */\n");
                buf.append("select 1\n");
                buf.append("from \n");
                buf.append("hoge\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertNotNull(reader.readSql());
        assertEquals(4, reader.getLineNumber());
    }
}
