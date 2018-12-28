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

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/** @author shinsuke-oda */
public class MssqlPagingTransformerTest extends TestCase {

  public void testOffsetLimit() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id offset 5 rows fetch next 10 rows only";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, 10, false);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetLimit_forceOffsetFetch() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id offset 5 rows fetch next 10 rows only";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, 10, true);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetLimit_option() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id  offset 5 rows fetch next 10 rows only option (maxrecursion 0)";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, 10, false);
    SqlParser parser =
        new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly() throws Exception {
    String expected = "select emp.id from emp order by emp.id offset 5 rows";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, -1, false);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly_forceOffsetFetch() throws Exception {
    String expected = "select emp.id from emp order by emp.id offset 5 rows";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, -1, true);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly_option() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id  offset 5 rows option (maxrecursion 0)";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, -1, false);
    SqlParser parser =
        new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly() throws Exception {
    String expected = "select top (10) emp.id from emp order by emp.id";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(-1, 10, false);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_forceOffsetFetch() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id offset 0 rows fetch next 10 rows only";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(-1, 10, true);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_option() throws Exception {
    String expected = "select top (10) emp.id from emp order by emp.id option (maxrecursion 0)";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(-1, 10, false);
    SqlParser parser =
        new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_option_forceOffsetFetch() throws Exception {
    String expected =
        "select emp.id from emp order by emp.id  offset 0 rows fetch next 10 rows only option (maxrecursion 0)";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(-1, 10, true);
    SqlParser parser =
        new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
