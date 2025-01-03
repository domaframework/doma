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
package org.seasar.doma.internal.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

public class PostgresPagingTransformerTest {

  @Test
  public void testOffsetLimit() {
    String expected = "select * from emp order by emp.id limit 10 offset 5";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetLimit_forUpdate() {
    String expected = "select * from emp order by emp.id  limit 10 offset 5 for update";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id for update");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly() {
    String expected = "select * from emp order by emp.id offset 5";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(5, -1);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testLimitOnly() {
    String expected = "select * from emp order by emp.id limit 10";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(-1, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetLimit_after_ForBlock() {
    String expected = "with fuga as (select * from hoge) select * from fuga  limit 10 offset 5";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("/*%for each : list*//*#each*//*%end*/ select * from fuga");
    SqlNode originalSqlNode = parser.parse();
    SqlNode sqlNode = transformer.transform(originalSqlNode);
    Config config = new MockConfig();
    Map<String, Value> map = new HashMap<>();
    map.put(
        "list",
        new Value(List.class, Collections.singletonList("with fuga as (select * from hoge)")));
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            map, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.RAW);
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetLimit_after_IfBlock() {
    String expected = "with fuga as (select * from hoge) select * from fuga  limit 10 offset 5";
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(5, 10);
    SqlParser parser =
        new SqlParser("/*%if true*/with fuga as (select * from hoge)/*%end*/ select * from fuga");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
