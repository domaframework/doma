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
package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import example.domain.PhoneNumber;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class SqlParserTest {

  private final MockConfig config = new MockConfig();

  private final MockConfig inListPaddingConfig =
      new MockConfig() {

        @Override
        public SqlBuilderSettings getSqlBuilderSettings() {
          return new SqlBuilderSettings() {

            @Override
            public boolean shouldRequireInListPadding() {
              return true;
            }
          };
        }
      };

  @Test
  public void testBindVariable() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testBindVariable_domain() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("phone", new Value(PhoneNumber.class, new PhoneNumber("01-2345-6789")));
    String testSql = "select * from aaa where phone = /*phone*/'111'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where phone = ?", sql.getRawSql());
    assertEquals("select * from aaa where phone = '01-2345-6789'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("01-2345-6789", sql.getParameters().get(0).getWrapper().get());
  }

  @Test
  public void testBindVariable_in_iterable() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo")));
    String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testBindVariable_in_array() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String[].class, new String[] {"hoge", "foo"}));
    String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testBindVariable_in_empty_iterable() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Collections.emptyList()));
    String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (null)", sql.getRawSql());
    assertEquals("select * from aaa where ename in (null)", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_in_padding() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo", "bar", "baz", "qux")));
    String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                inListPaddingConfig, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (?, ?, ?, ?, ?, ?, ?, ?)", sql.getRawSql());
    assertEquals(
        "select * from aaa where ename in ('hoge', 'foo', 'bar', 'baz', 'qux', 'qux', 'qux', 'qux')",
        sql.getFormattedSql());
    assertEquals(8, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
    assertEquals("bar", sql.getParameters().get(2).getWrapper().get());
    assertEquals("baz", sql.getParameters().get(3).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(4).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(5).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(6).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(7).getWrapper().get());
  }

  @Test
  public void testBindVariable_in_padding_empty() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, List.of()));
    String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                inListPaddingConfig, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (null)", sql.getRawSql());
    assertEquals("select * from aaa where ename in (null)", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_in_padding_comment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo", "bar", "baz", "qux")));
    String testSql = "select * from aaa where ename in /** comment */ /*name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                inListPaddingConfig, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from aaa where ename in /** comment */ (?, ?, ?, ?, ?, ?, ?, ?)",
        sql.getRawSql());
    assertEquals(
        "select * from aaa where ename in /** comment */ ('hoge', 'foo', 'bar', 'baz', 'qux', 'qux', 'qux', 'qux')",
        sql.getFormattedSql());
    assertEquals(8, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
    assertEquals("bar", sql.getParameters().get(2).getWrapper().get());
    assertEquals("baz", sql.getParameters().get(3).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(4).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(5).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(6).getWrapper().get());
    assertEquals("qux", sql.getParameters().get(7).getWrapper().get());
  }

  @Test
  public void testBindVariable_endsWithBindVariableComment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    String testSql = "select * from aaa where ename = /*name*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2110, expected.getMessageResource());
    }
  }

  @Test
  public void testBindVariable_emptyName() {
    String testSql = "select * from aaa where ename = /*   */'aaa'";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2120, expected.getMessageResource());
    }
  }

  @Test
  public void testBindVariable_stringLiteral() {
    String testSql = "select * from aaa where ename = /*name*/'bbb'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_intLiteral() {
    String testSql = "select * from aaa where ename = /*name*/10";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_floatLiteral() {
    String testSql = "select * from aaa where ename = /*name*/.0";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_booleanTrueLiteral() {
    String testSql = "select * from aaa where ename = /*name*/true";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_booleanFalseLiteral() {
    String testSql = "select * from aaa where ename = /*name*/false";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_nullLiteral() {
    String testSql = "select * from aaa where ename = /*name*/null";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testBindVariable_illegalLiteral() {
    String testSql = "select * from aaa where ename = /*name*/bbb";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2142, expected.getMessageResource());
    }
  }

  @Test
  public void testBindVariable_enum() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(MyEnum.class, MyEnum.BBB));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'BBB' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals(MyEnum.BBB, sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testLiteralVariable() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    String testSql = "select * from aaa where ename = /*^name*/'aaa' and sal = /*^salary*/-2000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testLiteralVariable_emptyName() {
    String testSql = "select * from aaa where ename = /*^   */'aaa'";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2228, expected.getMessageResource());
    }
  }

  @Test
  public void testLiteralVariable_in() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo")));
    String testSql = "select * from aaa where ename in /*^name*/('aaa', 'bbb')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getRawSql());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testLiteralVariable_endsWithLiteralVariableComment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    String testSql = "select * from aaa where ename = /*^name*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2110, expected.getMessageResource());
    }
  }

  @Test
  public void testLiteralVariable_illegalLiteral() {
    String testSql = "select * from aaa where ename = /*^name*/bbb";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2142, expected.getMessageResource());
    }
  }

  @Test
  public void testEmbeddedVariable() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "order by name asc, salary"));
    String testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from aaa where ename = ? and sal = ? order by name asc, salary", sql.getRawSql());
    assertEquals(
        "select * from aaa where ename = 'hoge' and sal = 10000 order by name asc, salary",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testEmbeddedVariable_inside() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("table", new Value(String.class, "aaa"));
    String testSql =
        "select * from /*# table */ where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testEmbeddedVariable_emptyName() {
    String testSql = "select * from aaa where ename = /*#   */'aaa'";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2121, expected.getMessageResource());
    }
  }

  @Test
  public void testExpand() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql = "select /*%expand*/* from aaa";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config,
                SqlKind.SELECT,
                "dummyPath",
                evaluator,
                SqlLogType.FORMATTED,
                node -> Arrays.asList("bbb", "ccc"))
            .build(sqlNode, Function.identity());
    assertEquals("select bbb, ccc from aaa", sql.getRawSql());
    assertEquals("select bbb, ccc from aaa", sql.getFormattedSql());
  }

  @Test
  public void testExpand_withSpace() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql = "select /*%expand */* from aaa";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config,
                SqlKind.SELECT,
                "dummyPath",
                evaluator,
                SqlLogType.FORMATTED,
                node -> Arrays.asList("bbb", "ccc"))
            .build(sqlNode, Function.identity());
    assertEquals("select bbb, ccc from aaa", sql.getRawSql());
    assertEquals("select bbb, ccc from aaa", sql.getFormattedSql());
  }

  @Test
  public void testExpand_alias() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql = "select /*%expand \"a\"*/* from aaa a";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config,
                SqlKind.SELECT,
                "dummyPath",
                evaluator,
                SqlLogType.FORMATTED,
                node -> Arrays.asList("bbb", "ccc"))
            .build(sqlNode, Function.identity());
    assertEquals("select a.bbb, a.ccc from aaa a", sql.getRawSql());
    assertEquals("select a.bbb, a.ccc from aaa a", sql.getFormattedSql());
  }

  @Test
  public void testExpand_notAsteriskChar() {
    String testSql = "select /*%expand*/+ from aaa";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2143, expected.getMessageResource());
    }
  }

  @Test
  public void testExpand_word() {
    String testSql = "select /*%expand*/'hoge' from aaa";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2143, expected.getMessageResource());
    }
  }

  @Test
  public void testIf() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where bbb = 'hoge'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
  }

  @Test
  public void testIf_fromClause() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("type", new Value(String.class, "a"));
    String testSql = "select * from /*%if type == \"a\"*/aaa/*%else*/ bbb/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
  }

  @Test
  public void testIf_selectClause() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("type", new Value(String.class, "a"));
    String testSql = "select /*%if type == \"a\"*/aaa /*%else*/ bbb /*%end*/from ccc";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select aaa from ccc", sql.getRawSql());
    assertEquals("select aaa from ccc", sql.getFormattedSql());
  }

  @Test
  public void testIf_removeWhere() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testIf_removeOrderBy() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql = "select * from aaa order by /*%if name != null*/bbb/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testIf_removeGroupBy() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql = "select * from aaa group by /*%if name != null*/bbb/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testIf_removeAnd() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql =
        "select * from aaa where \n/*%if name != null*/bbb = /*name*/'ccc' \n/*%else*/\n --comment\nand ddd is null\n /*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where \n\n --comment\n ddd is null", sql.getRawSql());
    assertEquals("select * from aaa where \n\n --comment\n ddd is null", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testIf_nest() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    String testSql =
        "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%if name == \"hoge\"*/and ddd = eee/*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where bbb = ? and ddd = eee", sql.getRawSql());
    assertEquals("select * from aaa where bbb = 'hoge' and ddd = eee", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testIf_nestContinuously() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("name2", new Value(String.class, null));
    String testSql =
        "select * from aaa where /*%if name != null*//*%if name2 == \"hoge\"*/ ddd = eee/*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testElseifBlock() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, ""));
    String testSql =
        "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name ==\"\"*/\nbbb = /*name*/'ccc'/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where \nbbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where \nbbb = ''", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("", sql.getParameters().get(0).getWrapper().get());
  }

  @Test
  public void testElseBlock() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    String testSql =
        "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name == \"\"*/\n/*%else*/ bbb = /*name*/'ccc'/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where  bbb = 'hoge'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
  }

  @Test
  public void testUnion() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql = "select * from aaa where /*%if false*//*%end*/union all select * from bbb";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa union all select * from bbb", sql.getRawSql());
  }

  @Test
  public void testSelect() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("count", new Value(Integer.class, 5));
    String testSql =
        "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = /*name*/'ccc' group by aaa.deptname having count(*) > /*count*/10 order by aaa.name for update bbb";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = ? group by aaa.deptname having count(*) > ? order by aaa.name for update bbb",
        sql.getRawSql());
    assertEquals(
        "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = 'hoge' group by aaa.deptname having count(*) > 5 order by aaa.name for update bbb",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(5, sql.getParameters().get(1).getWrapper().get());
  }

  @Test
  public void testUpdate() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("no", new Value(Integer.class, 10));
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("id", new Value(Integer.class, 100));
    String testSql = "update aaa set no = /*no*/1, set name = /*name*/'name' where id = /*id*/1";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("update aaa set no = ?, set name = ? where id = ?", sql.getRawSql());
    assertEquals("update aaa set no = 10, set name = 'hoge' where id = 100", sql.getFormattedSql());
    assertEquals(3, sql.getParameters().size());
    assertEquals(10, sql.getParameters().get(0).getWrapper().get());
    assertEquals("hoge", sql.getParameters().get(1).getWrapper().get());
    assertEquals(100, sql.getParameters().get(2).getWrapper().get());
  }

  @Test
  public void testFor() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where name = ? or name = ? or name = ?", sql.getRawSql());
    assertEquals(
        "select * from aaa where name = 'aaa' or name = 'bbb' or name = 'ccc'",
        sql.getFormattedSql());
    assertEquals(3, sql.getParameters().size());
    assertEquals("aaa", sql.getParameters().get(0).getWrapper().get());
    assertEquals("bbb", sql.getParameters().get(1).getWrapper().get());
    assertEquals("ccc", sql.getParameters().get(2).getWrapper().get());
  }

  @Test
  public void testFor_array() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String[] array = new String[] {"aaa", "bbb", "ccc"};
    evaluator.add("names", new Value(String[].class, array));
    String testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where name = ? or name = ? or name = ?", sql.getRawSql());
    assertEquals(
        "select * from aaa where name = 'aaa' or name = 'bbb' or name = 'ccc'",
        sql.getFormattedSql());
    assertEquals(3, sql.getParameters().size());
    assertEquals("aaa", sql.getParameters().get(0).getWrapper().get());
    assertEquals("bbb", sql.getParameters().get(1).getWrapper().get());
    assertEquals("ccc", sql.getParameters().get(2).getWrapper().get());
  }

  @Test
  public void testFor_removeWhere() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testFor_removeOrderBy() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa order by /*%for n : names*/name = /*n*/'a' /*%if n_has_next */, /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testFor_removeGroupBy() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa group by /*%for n : names*/name = /*n*/'a' /*%if n_has_next */, /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testFor_removeOr() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/ or salary > 100";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where   salary > 100", sql.getRawSql());
    assertEquals("select * from aaa where   salary > 100", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testParserLevelBlockComment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    SqlParser parser = new SqlParser("select /*%! comment */a from b");
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select a from b", sql.getRawSql());
  }

  @Test
  public void testFor_index() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    ArrayList<String> list = new ArrayList<>();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    evaluator.add("names", new Value(List.class, list));
    String testSql =
        "select * from aaa where /*%for n : names*/name/*# n_index */ = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where name0 = ? or name1 = ? or name2 = ?", sql.getRawSql());
    assertEquals(
        "select * from aaa where name0 = 'aaa' or name1 = 'bbb' or name2 = 'ccc'",
        sql.getFormattedSql());
    assertEquals(3, sql.getParameters().size());
    assertEquals("aaa", sql.getParameters().get(0).getWrapper().get());
    assertEquals("bbb", sql.getParameters().get(1).getWrapper().get());
    assertEquals("ccc", sql.getParameters().get(2).getWrapper().get());
  }

  @Test
  public void testValidate_ifEnd() {
    SqlParser parser = new SqlParser("select * from aaa /*%if true*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  @Test
  public void testValidate_ifEnd_selectClause() {
    SqlParser parser = new SqlParser("select /*%if true*/* from aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  @Test
  public void testValidate_ifEnd_subquery() {
    SqlParser parser = new SqlParser("select *, (select /*%if true */ from aaa) x from aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  @Test
  public void testValidate_forEnd() {
    SqlParser parser = new SqlParser("select * from aaa /*%for name : names*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2134, expected.getMessageResource());
    }
  }

  @Test
  public void testValidate_unclosedParens() {
    SqlParser parser = new SqlParser("select * from (select * from bbb");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2135, expected.getMessageResource());
    }
  }

  @Test
  public void testValidate_enclosedParensByIfBlock() {
    SqlParser parser = new SqlParser("select * from /*%if true*/(select * from bbb)/*%end*/");
    parser.parse();
  }

  @Test
  public void testParens_removeAnd() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql =
        "select * from aaa where (\n/*%if name != null*/bbb = /*name*/'ccc'\n/*%else*/\nand ddd is null\n /*%end*/)";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where (\n\n ddd is null\n )", sql.getRawSql());
    assertEquals("select * from aaa where (\n\n ddd is null\n )", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  @Test
  public void testEmptyParens() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    SqlParser parser = new SqlParser("select rank()");
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select rank()", sql.getRawSql());
  }

  @Test
  public void testEmptyParens_whiteSpace() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    SqlParser parser = new SqlParser("select rank(   )");
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select rank(   )", sql.getRawSql());
  }

  @Test
  public void testManyEol() throws WrapException {
    String path = "META-INF/" + getClass().getName().replace('.', '/') + "/manyEol.sql";
    String sql = ResourceUtil.getResourceAsString(path);
    SqlParser parser = new SqlParser(sql);
    SqlNode sqlNode = parser.parse();
    assertNotNull(sqlNode);
  }

  @Test
  public void testDelimiter() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    SqlParser parser = new SqlParser("select 1; select 2;");
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select 1;", sql.getRawSql());
  }

  @Test
  public void testBindVariable_multipleInClauses() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("names", new Value(List.class, Arrays.asList("hoge", "foo")));
    evaluator.add("ids", new Value(List.class, Arrays.asList(1, 2, 3)));
    String testSql =
        "select * from aaa where ename in /*names*/('aaa', 'bbb') and id in /*ids*/(1, 2)";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (?, ?) and id in (?, ?, ?)", sql.getRawSql());
    assertEquals(
        "select * from aaa where ename in ('hoge', 'foo') and id in (1, 2, 3)",
        sql.getFormattedSql());
    assertEquals(5, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_nullValue() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    String testSql = "select * from aaa where ename = /*name*/'test'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = null", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testEmbeddedVariable_complexExpression() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add(
        "condition", new Value(String.class, "status = 'ACTIVE' AND created_date > '2023-01-01'"));
    String testSql = "select * from users where /*# condition */";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    var ex =
        assertThrows(
            JdbcException.class,
            () -> {
              new NodePreparedSqlBuilder(
                      config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
                  .build(sqlNode, Function.identity());
            });
    assertEquals(Message.DOMA2116, ex.getMessageResource());
  }

  @Test
  public void testIf_multipleConditions() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    evaluator.add("status", new Value(String.class, "ACTIVE"));
    String testSql =
        "select * from aaa where /*%if name != null && status != null*/name = /*name*/'default' and status = /*status*/'INACTIVE'/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where name = ? and status = ?", sql.getRawSql());
    assertEquals(
        "select * from aaa where name = 'test' and status = 'ACTIVE'", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testIf_complexElseifChain() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("type", new Value(String.class, "PREMIUM"));
    String testSql =
        "select * from users where /*%if type == \"BASIC\"*/price < 100/*%elseif type == \"STANDARD\"*/price BETWEEN 100 AND 500/*%elseif type == \"PREMIUM\"*/price > 500/*%else*/price IS NULL/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from users where price > 500", sql.getRawSql());
    assertEquals("select * from users where price > 500", sql.getFormattedSql());
  }

  @Test
  public void testFor_nestedLoop() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("categories", new Value(List.class, Arrays.asList("A", "B")));
    evaluator.add("statuses", new Value(List.class, Arrays.asList("ACTIVE", "INACTIVE")));
    String testSql =
        "select * from products where /*%for cat : categories*/(category = /*cat*/'DEFAULT' /*%for status : statuses*/AND status = /*status*/'UNKNOWN'/*%if status_has_next*/ OR /*%end*//*%end*/)/*%if cat_has_next*/ OR /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from products where (category = ? AND status = ? OR AND status = ?) OR (category = ? AND status = ? OR AND status = ?)",
        sql.getRawSql());
    assertEquals(6, sql.getParameters().size());
  }

  @Test
  public void testComplexSubquery() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("minSalary", new Value(BigDecimal.class, new BigDecimal(50000)));
    evaluator.add("dept", new Value(String.class, "IT"));
    String testSql =
        "select e.*, (select count(*) from projects p where p.emp_id = e.id /*%if dept != null*/and p.department = /*dept*/'DEFAULT'/*%end*/) as project_count from employees e where e.salary > /*minSalary*/0";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select e.*, (select count(*) from projects p where p.emp_id = e.id and p.department = ?) as project_count from employees e where e.salary > ?",
        sql.getRawSql());
    assertEquals(
        "select e.*, (select count(*) from projects p where p.emp_id = e.id and p.department = 'IT') as project_count from employees e where e.salary > 50000",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testWindowFunction() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("partitionCol", new Value(String.class, "department"));
    String testSql =
        "select *, row_number() over (partition by /*# partitionCol */ order by salary desc) as rank from employees";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select *, row_number() over (partition by department order by salary desc) as rank from employees",
        sql.getRawSql());
    assertEquals(
        "select *, row_number() over (partition by department order by salary desc) as rank from employees",
        sql.getFormattedSql());
  }

  @Test
  public void testCTE_commonTableExpression() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("year", new Value(Integer.class, 2023));
    String testSql =
        "with recent_orders as (select * from orders where year = /*year*/2022) select * from recent_orders where amount > 1000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "with recent_orders as (select * from orders where year = ?) select * from recent_orders where amount > 1000",
        sql.getRawSql());
    assertEquals(
        "with recent_orders as (select * from orders where year = 2023) select * from recent_orders where amount > 1000",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testCaseExpression() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("threshold", new Value(Integer.class, 100));
    String testSql =
        "select case when amount > /*threshold*/50 then 'HIGH' when amount > 25 then 'MEDIUM' else 'LOW' end as category from orders";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select case when amount > ? then 'HIGH' when amount > 25 then 'MEDIUM' else 'LOW' end as category from orders",
        sql.getRawSql());
    assertEquals(
        "select case when amount > 100 then 'HIGH' when amount > 25 then 'MEDIUM' else 'LOW' end as category from orders",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testJoinWithDynamicConditions() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("includeInactive", new Value(Boolean.class, true));
    evaluator.add("deptId", new Value(Integer.class, 10));
    String testSql =
        "select e.*, d.name from employees e join departments d on e.dept_id = d.id /*%if includeInactive*/left join user_status us on e.id = us.emp_id/*%end*/ where d.id = /*deptId*/1 /*%if includeInactive*/and (us.status is null or us.status = 'ACTIVE')/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select e.*, d.name from employees e join departments d on e.dept_id = d.id left join user_status us on e.id = us.emp_id where d.id = ? and (us.status is null or us.status = 'ACTIVE')",
        sql.getRawSql());
    assertEquals(
        "select e.*, d.name from employees e join departments d on e.dept_id = d.id left join user_status us on e.id = us.emp_id where d.id = 10 and (us.status is null or us.status = 'ACTIVE')",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testRecursiveCTE() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("startId", new Value(Integer.class, 1));
    String testSql =
        "with recursive employee_hierarchy as (select id, name, manager_id, 0 as level from employees where id = /*startId*/1 union all select e.id, e.name, e.manager_id, eh.level + 1 from employees e join employee_hierarchy eh on e.manager_id = eh.id) select * from employee_hierarchy";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "with recursive employee_hierarchy as (select id, name, manager_id, 0 as level from employees where id = ? union all select e.id, e.name, e.manager_id, eh.level + 1 from employees e join employee_hierarchy eh on e.manager_id = eh.id) select * from employee_hierarchy",
        sql.getRawSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testMultipleExpand() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql =
        "select /*%expand*/*, /*%expand \"u\"*/* from users u join orders o on u.id = o.user_id";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config,
                SqlKind.SELECT,
                "dummyPath",
                evaluator,
                SqlLogType.FORMATTED,
                node -> {
                  return Arrays.asList("id", "name", "email", "created_at");
                })
            .build(sqlNode, Function.identity());
    assertEquals(
        "select id, name, email, created_at, u.id, u.name, u.email, u.created_at from users u join orders o on u.id = o.user_id",
        sql.getRawSql());
  }

  @Test
  public void testComplexForLoop_withIndex() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("filters", new Value(List.class, Arrays.asList("name", "email", "phone")));
    String testSql =
        "select * from users where /*%for filter : filters*//*# filter */ like /* \"%\" + filter + \"_\" + @java.lang.String@valueOf(filter_index) + \"%\" */'test' /*%if filter_has_next*/and /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from users where name like ? and email like ? and phone like ?", sql.getRawSql());
    assertEquals(
        "select * from users where name like '%name_0%' and email like '%email_1%' and phone like '%phone_2%'",
        sql.getFormattedSql());
  }

  @Test
  public void testInvalidBindVariableName_specialChars() {
    String testSql = "select * from aaa where ename = /*name-with-dash*/'test'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode node = parser.parse();
    assertNotNull(node);
  }

  @Test
  public void testLongSqlStatement() {
    StringBuilder longSql = new StringBuilder("select ");
    for (int i = 0; i < 100; i++) {
      if (i > 0) longSql.append(", ");
      longSql.append("col").append(i);
    }
    longSql.append(" from very_long_table_name_that_exceeds_normal_limits where id = /*id*/1");

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("id", new Value(Integer.class, 123));
    SqlParser parser = new SqlParser(longSql.toString());
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(1, sql.getParameters().size());
    assertEquals(123, sql.getParameters().get(0).getWrapper().get());
  }

  @Test
  public void testUnicodeInSql() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "山田太郎"));
    String testSql = "select * from 社員テーブル where 名前 = /*name*/'テスト' and 部署 = 'エンジニアリング'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from 社員テーブル where 名前 = ? and 部署 = 'エンジニアリング'", sql.getRawSql());
    assertEquals(
        "select * from 社員テーブル where 名前 = '山田太郎' and 部署 = 'エンジニアリング'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testInvalidBindVariable_unclosedComment() {
    String testSql = "select * from aaa where ename = /*name 'test'";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2102, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidEmbeddedVariable_unclosedComment() {
    String testSql = "select * from aaa where /*# condition";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2102, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidFor_missingColon() {
    String testSql = "select * from aaa where /*%for item items*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2124, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidFor_emptyVariableName() {
    String testSql = "select * from aaa where /*%for  : items*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2125, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidFor_emptyIterableName() {
    String testSql = "select * from aaa where /*%for item : *//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2126, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidIf_emptyCondition() {
    String testSql = "select * from aaa where /*%if */condition/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2241, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidElseif_emptyCondition() {
    String testSql = "select * from aaa where /*%if false*/a/*%elseif */b/*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2242, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidEnd_withoutDirective() {
    String testSql = "select * from aaa where condition /*%end*/";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2104, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidElse_withoutIf() {
    String testSql = "select * from aaa where /*%else*/condition";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2140, expected.getMessageResource());
    }
  }

  @Test
  public void testInvalidElseif_withoutIf() {
    String testSql = "select * from aaa where /*%elseif true*/condition";
    SqlParser parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2138, expected.getMessageResource());
    }
  }

  @Test
  public void testBindVariable_escapedSingleQuote() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "John's car"));
    String testSql = "select * from aaa where description = /*name*/'test''s value'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where description = ?", sql.getRawSql());
    // We do not escape single quotes included in the test data.
    assertEquals("select * from aaa where description = 'John's car'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testLiteralVariable_escapedSingleQuote() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "John's car"));
    String testSql = "select * from aaa where description = /*^name*/'test''s value'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    var ex =
        assertThrows(
            JdbcException.class,
            () -> {
              new NodePreparedSqlBuilder(
                      config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
                  .build(sqlNode, Function.identity());
            });
    assertEquals(Message.DOMA2224, ex.getMessageResource());
  }

  @Test
  public void testBindVariable_withLineBreaksInComment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    String testSql = "select * from aaa where ename = /*\n  name\n  */'default'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'test'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_multipleWhitespaceInComment() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    String testSql = "select * from aaa where ename = /*     name     */'default'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'test'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testEmptyStringLiteral() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    String testSql = "select * from aaa where ename = /*name*/''";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'test'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_consecutiveComments() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    evaluator.add("age", new Value(Integer.class, 25));
    String testSql =
        "select * from aaa where ename = /*name*/'default' /**comment*/ and age = /*age*/0 /**another comment*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from aaa where ename = ? /**comment*/ and age = ? /**another comment*/",
        sql.getRawSql());
    assertEquals(
        "select * from aaa where ename = 'test' /**comment*/ and age = 25 /**another comment*/",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testBindVariable_withTabsAndSpaces() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "test"));
    String testSql = "select\t*\tfrom\taaa\twhere\tename\t=\t/*name*/'default'\t";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select\t*\tfrom\taaa\twhere\tename\t=\t?", sql.getRawSql());
    assertEquals("select\t*\tfrom\taaa\twhere\tename\t=\t'test'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testFor_singleElement() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("items", new Value(List.class, List.of("single")));
    String testSql =
        "select * from aaa where /*%for item : items*/name = /*item*/'default'/*%if item_has_next*/ or /*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where name = ?", sql.getRawSql());
    assertEquals("select * from aaa where name = 'single'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testIf_deeplyNested() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("level1", new Value(Boolean.class, true));
    evaluator.add("level2", new Value(Boolean.class, true));
    evaluator.add("level3", new Value(Boolean.class, true));
    String testSql =
        "select * from aaa where /*%if level1*/L1 /*%if level2*/AND L2 /*%if level3*/AND L3/*%end*//*%end*//*%end*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where L1 AND L2 AND L3", sql.getRawSql());
    assertEquals("select * from aaa where L1 AND L2 AND L3", sql.getFormattedSql());
  }

  @Test
  public void testMergeStatement() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("id", new Value(Integer.class, 1));
    evaluator.add("name", new Value(String.class, "John"));
    String testSql =
        "merge into target_table using source_table on target_table.id = /*id*/1 when matched then update set name = /*name*/'default' when not matched then insert (id, name) values (/*id*/1, /*name*/'default')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "merge into target_table using source_table on target_table.id = ? when matched then update set name = ? when not matched then insert (id, name) values (?, ?)",
        sql.getRawSql());
    assertEquals(
        "merge into target_table using source_table on target_table.id = 1 when matched then update set name = 'John' when not matched then insert (id, name) values (1, 'John')",
        sql.getFormattedSql());
    assertEquals(4, sql.getParameters().size());
  }

  @Test
  public void testDeleteWithJoin() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("status", new Value(String.class, "INACTIVE"));
    String testSql =
        "delete e from employees e join departments d on e.dept_id = d.id where d.status = /*status*/'ACTIVE'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.DELETE, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "delete e from employees e join departments d on e.dept_id = d.id where d.status = ?",
        sql.getRawSql());
    assertEquals(
        "delete e from employees e join departments d on e.dept_id = d.id where d.status = 'INACTIVE'",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testUpdateWithSubquery() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("minSalary", new Value(BigDecimal.class, new BigDecimal(50000)));
    String testSql =
        "update employees set bonus = (select avg(salary) * 0.1 from employees where salary > /*minSalary*/0) where department = 'SALES'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.UPDATE, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "update employees set bonus = (select avg(salary) * 0.1 from employees where salary > ?) where department = 'SALES'",
        sql.getRawSql());
    assertEquals(
        "update employees set bonus = (select avg(salary) * 0.1 from employees where salary > 50000) where department = 'SALES'",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testInsertWithSelectAndUnion() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("year", new Value(Integer.class, 2023));
    String testSql =
        "insert into summary_table select dept, count(*), /*year*/2022 from employees group by dept union all select 'TOTAL', count(*), /*year*/2022 from employees";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.INSERT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "insert into summary_table select dept, count(*), ? from employees group by dept union all select 'TOTAL', count(*), ? from employees",
        sql.getRawSql());
    assertEquals(
        "insert into summary_table select dept, count(*), 2023 from employees group by dept union all select 'TOTAL', count(*), 2023 from employees",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testExistsSubquery() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("status", new Value(String.class, "ACTIVE"));
    String testSql =
        "select * from employees e where exists (select 1 from projects p where p.emp_id = e.id and p.status = /*status*/'PENDING')";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from employees e where exists (select 1 from projects p where p.emp_id = e.id and p.status = ?)",
        sql.getRawSql());
    assertEquals(
        "select * from employees e where exists (select 1 from projects p where p.emp_id = e.id and p.status = 'ACTIVE')",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testPivotOperation() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("year", new Value(Integer.class, 2023));
    String testSql =
        "select * from (select dept, quarter, sales from sales_data where year = /*year*/2022) pivot (sum(sales) for quarter in ('Q1' as q1, 'Q2' as q2, 'Q3' as q3, 'Q4' as q4))";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from (select dept, quarter, sales from sales_data where year = ?) pivot (sum(sales) for quarter in ('Q1' as q1, 'Q2' as q2, 'Q3' as q3, 'Q4' as q4))",
        sql.getRawSql());
    assertEquals(
        "select * from (select dept, quarter, sales from sales_data where year = 2023) pivot (sum(sales) for quarter in ('Q1' as q1, 'Q2' as q2, 'Q3' as q3, 'Q4' as q4))",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testLateralJoin() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("limit", new Value(Integer.class, 5));
    String testSql =
        "select * from departments d, lateral (select * from employees e where e.dept_id = d.id order by salary desc limit /*limit*/10) top_employees";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from departments d, lateral (select * from employees e where e.dept_id = d.id order by salary desc limit ?) top_employees",
        sql.getRawSql());
    assertEquals(
        "select * from departments d, lateral (select * from employees e where e.dept_id = d.id order by salary desc limit 5) top_employees",
        sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  @Test
  public void testGenerateSeries() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("start", new Value(Integer.class, 1));
    evaluator.add("end", new Value(Integer.class, 100));
    String testSql =
        "select * from generate_series(/*start*/1, /*end*/10) as t(id) join users u on u.id = t.id";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from generate_series(?, ?) as t(id) join users u on u.id = t.id",
        sql.getRawSql());
    assertEquals(
        "select * from generate_series(1, 100) as t(id) join users u on u.id = t.id",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testComplexAnalyticFunction() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("partitionBy", new Value(String.class, "department"));
    String testSql =
        "select *, lag(salary, 1) over (partition by /*# partitionBy */ order by hire_date), lead(salary, 1) over (partition by /*# partitionBy */ order by hire_date) from employees";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select *, lag(salary, 1) over (partition by department order by hire_date), lead(salary, 1) over (partition by department order by hire_date) from employees",
        sql.getRawSql());
    assertEquals(
        "select *, lag(salary, 1) over (partition by department order by hire_date), lead(salary, 1) over (partition by department order by hire_date) from employees",
        sql.getFormattedSql());
  }

  @Test
  public void testBindVariable_withJsonPath() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("jsonPath", new Value(String.class, "$.user.profile.email"));
    evaluator.add("searchValue", new Value(String.class, "john@example.com"));
    String testSql =
        "select * from users where json_extract(data, /*jsonPath*/'$.default') = /*searchValue*/'default'";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from users where json_extract(data, ?) = ?", sql.getRawSql());
    assertEquals(
        "select * from users where json_extract(data, '$.user.profile.email') = 'john@example.com'",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testComplexAggregateWithFilter() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("year", new Value(Integer.class, 2023));
    evaluator.add("minAmount", new Value(BigDecimal.class, new BigDecimal(1000)));
    String testSql =
        "select dept, count(*) filter (where hire_year = /*year*/2022), sum(salary) filter (where salary > /*minAmount*/0) from employees group by dept";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select dept, count(*) filter (where hire_year = ?), sum(salary) filter (where salary > ?) from employees group by dept",
        sql.getRawSql());
    assertEquals(
        "select dept, count(*) filter (where hire_year = 2023), sum(salary) filter (where salary > 1000) from employees group by dept",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testComplexPaginationWithOffset() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("pageSize", new Value(Integer.class, 20));
    evaluator.add("offset", new Value(Integer.class, 100));
    evaluator.add("orderBy", new Value(String.class, "created_date desc, id asc"));
    String testSql =
        "select * from large_table where active = true order by /*# orderBy */ limit /*pageSize*/10 offset /*offset*/0";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(
        "select * from large_table where active = true order by created_date desc, id asc limit ? offset ?",
        sql.getRawSql());
    assertEquals(
        "select * from large_table where active = true order by created_date desc, id asc limit 20 offset 100",
        sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
  }

  @Test
  public void testPerformanceTestForLargeInClause() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    List<Integer> largeList = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      largeList.add(i);
    }
    evaluator.add("ids", new Value(List.class, largeList));
    String testSql = "select * from large_table where id in /*ids*/(1, 2, 3)";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals(1000, sql.getParameters().size());
  }

  public enum MyEnum {
    AAA,
    BBB,
    CCC
  }
}
