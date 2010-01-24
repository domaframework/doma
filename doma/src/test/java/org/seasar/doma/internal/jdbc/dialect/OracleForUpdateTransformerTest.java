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
package org.seasar.doma.internal.jdbc.dialect;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class OracleForUpdateTransformerTest extends TestCase {

    public void testForUpdateNormal() throws Exception {
        String expected = "select * from emp order by emp.id for update";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new MockConfig(), SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateNormal_alias() throws Exception {
        String expected = "select * from emp order by emp.id for update of emp.name, emp.salary";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0, "emp.name", "emp.salary");
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new MockConfig(), SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateNowait() throws Exception {
        String expected = "select * from emp order by emp.id for update nowait";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NOWAIT, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new MockConfig(), SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateWait() throws Exception {
        String expected = "select * from emp order by emp.id for update wait 10";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.WAIT, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new MockConfig(), SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }
}
