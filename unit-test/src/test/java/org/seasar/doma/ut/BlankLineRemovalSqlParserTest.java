package org.seasar.doma.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/** This class is base on the org.seasar.doma.internal.jdbc.sql.SqlParserTest class.: */
public class BlankLineRemovalSqlParserTest {

  private final MockConfig config =
      new MockConfig() {

        @Override
        public SqlBuilderSettings getSqlBuilderSettings() {
          return new SqlBuilderSettings() {
            @Override
            public boolean shouldRemoveBlankLines() {
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
    assertEquals("select * from aaa where\n --comment\n ddd is null", sql.getRawSql());
    assertEquals("select * from aaa where\n --comment\n ddd is null", sql.getFormattedSql());
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
    assertEquals("select * from aaa where\nbbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where\nbbb = ''", sql.getFormattedSql());
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
    assertEquals("select * from aaa where (\n ddd is null\n )", sql.getRawSql());
    assertEquals("select * from aaa where (\n ddd is null\n )", sql.getFormattedSql());
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

  public enum MyEnum {
    AAA,
    BBB,
    CCC
  }
}
