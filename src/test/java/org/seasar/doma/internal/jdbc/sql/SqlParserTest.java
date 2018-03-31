package org.seasar.doma.internal.jdbc.sql;

import example.holder.PhoneNumber;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.message.Message;

public class SqlParserTest extends TestCase {

  private final MockConfig config = new MockConfig();

  public void testBindVariable() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    var testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  public void testBindVariable_holder() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("phone", new Value(PhoneNumber.class, new PhoneNumber("01-2345-6789")));
    var testSql = "select * from aaa where phone = /*phone*/'111'";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where phone = ?", sql.getRawSql());
    assertEquals("select * from aaa where phone = '01-2345-6789'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("01-2345-6789", sql.getParameters().get(0).getWrapper().get());
  }

  public void testBindVariable_in() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo")));
    var testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals("foo", sql.getParameters().get(1).getWrapper().get());
  }

  public void testBindVariable_in_empty_iterable() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Collections.emptyList()));
    var testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in (null)", sql.getRawSql());
    assertEquals("select * from aaa where ename in (null)", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testBindVariable_endsWithBindVariableComment() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    var testSql = "select * from aaa where ename = /*name*/";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2110, expected.getMessageResource());
    }
  }

  public void testBindVariable_emptyName() throws Exception {
    var testSql = "select * from aaa where ename = /*   */'aaa'";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2120, expected.getMessageResource());
    }
  }

  public void testBindVariable_stringLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/'bbb'";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_intLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/10";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_floatLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/.0";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_booleanTrueLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/true";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_booleanFalseLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/false";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_nullLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/null";
    var parser = new SqlParser(testSql);
    var node = parser.parse();
    assertNotNull(node);
  }

  public void testBindVariable_illegalLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*name*/bbb";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2142, expected.getMessageResource());
    }
  }

  public void testBindVariable_enum() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(MyEnum.class, MyEnum.BBB));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    var testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'BBB' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals(MyEnum.BBB, sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  public void testLiteralVariable() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    var testSql = "select * from aaa where ename = /*^name*/'aaa' and sal = /*^salary*/-2000";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testLiteralVariable_emptyName() throws Exception {
    var testSql = "select * from aaa where ename = /*^   */'aaa'";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2228, expected.getMessageResource());
    }
  }

  public void testLiteralVariable_in() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(List.class, Arrays.asList("hoge", "foo")));
    var testSql = "select * from aaa where ename in /*^name*/('aaa', 'bbb')";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getRawSql());
    assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testLiteralVariable_endsWithLiteralVariableComment() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    var testSql = "select * from aaa where ename = /*^name*/";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2110, expected.getMessageResource());
    }
  }

  public void testLiteralVariable_illegalLiteral() throws Exception {
    var testSql = "select * from aaa where ename = /*^name*/bbb";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2142, expected.getMessageResource());
    }
  }

  public void testEmbeddedVariable() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "order by name asc, salary"));
    var testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testEmbeddedVariable_inside() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("table", new Value(String.class, "aaa"));
    var testSql =
        "select * from /*# table */ where ename = /*name*/'aaa' and sal = /*salary*/-2000";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where ename = ? and sal = ?", sql.getRawSql());
    assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql.getFormattedSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(10000), sql.getParameters().get(1).getWrapper().get());
  }

  public void testEmbeddedVariable_emptyName() throws Exception {
    var testSql = "select * from aaa where ename = /*#   */'aaa'";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2121, expected.getMessageResource());
    }
  }

  public void testExpand() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var testSql = "select /*%expand*/* from aaa";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testExpand_withSpace() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var testSql = "select /*%expand */* from aaa";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testExpand_alias() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var testSql = "select /*%expand \"a\"*/* from aaa a";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testExpand_notAsteriskChar() throws Exception {
    var testSql = "select /*%expand*/+ from aaa";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2143, expected.getMessageResource());
    }
  }

  public void testExpand_word() throws Exception {
    var testSql = "select /*%expand*/'hoge' from aaa";
    var parser = new SqlParser(testSql);
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2143, expected.getMessageResource());
    }
  }

  public void testIf() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    var testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where bbb = 'hoge'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
  }

  public void testIf_fromClause() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("type", new Value(String.class, "a"));
    var testSql = "select * from /*%if type == \"a\"*/aaa/*%else*/ bbb/*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
  }

  public void testIf_selectClause() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("type", new Value(String.class, "a"));
    var testSql = "select /*%if type == \"a\"*/aaa /*%else*/ bbb /*%end*/from ccc";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select aaa from ccc", sql.getRawSql());
    assertEquals("select aaa from ccc", sql.getFormattedSql());
  }

  public void testIf_removeWhere() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    var testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testIf_removeAnd() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    var testSql =
        "select * from aaa where \n/*%if name != null*/bbb = /*name*/'ccc' \n/*%else*/\n --comment\nand ddd is null\n /*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where \n\n --comment\n ddd is null", sql.getRawSql());
    assertEquals("select * from aaa where \n\n --comment\n ddd is null", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testIf_nest() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    var testSql =
        "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%if name == \"hoge\"*/and ddd = eee/*%end*//*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where bbb = ? and ddd = eee", sql.getRawSql());
    assertEquals("select * from aaa where bbb = 'hoge' and ddd = eee", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
  }

  public void testIf_nestContinuously() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("name2", new Value(String.class, null));
    var testSql =
        "select * from aaa where /*%if name != null*//*%if name2 == \"hoge\"*/ ddd = eee/*%end*//*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testElseifBlock() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, ""));
    var testSql =
        "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name ==\"\"*/\nbbb = /*name*/'ccc'/*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where \nbbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where \nbbb = ''", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("", sql.getParameters().get(0).getWrapper().get());
  }

  public void testElseBlock() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    var testSql =
        "select * from aaa where /*%if name == null*/bbb is null\n/*%elseif name == \"\"*/\n/*%else*/ bbb = /*name*/'ccc'/*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
    assertEquals("select * from aaa where  bbb = 'hoge'", sql.getFormattedSql());
    assertEquals(1, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
  }

  public void testUnion() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var testSql = "select * from aaa where /*%if false*//*%end*/union all select * from bbb";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa union all select * from bbb", sql.getRawSql());
  }

  public void testSelect() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("count", new Value(Integer.class, 5));
    var testSql =
        "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = /*name*/'ccc' group by aaa.deptname having count(*) > /*count*/10 order by aaa.name for update bbb";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testUpdate() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("no", new Value(Integer.class, 10));
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("id", new Value(Integer.class, 100));
    var testSql = "update aaa set no = /*no*/1, set name = /*name*/'name' where id = /*id*/1";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testFor() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var list = new ArrayList<String>();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    evaluator.add("names", new Value(List.class, list));
    var testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testFor_removeWhere() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var list = new ArrayList<String>();
    evaluator.add("names", new Value(List.class, list));
    var testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
    assertEquals("select * from aaa", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testFor_removeOr() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var list = new ArrayList<String>();
    evaluator.add("names", new Value(List.class, list));
    var testSql =
        "select * from aaa where /*%for n : names*/name = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/ or salary > 100";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where   salary > 100", sql.getRawSql());
    assertEquals("select * from aaa where   salary > 100", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testFor_index() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var list = new ArrayList<String>();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    evaluator.add("names", new Value(List.class, list));
    var testSql =
        "select * from aaa where /*%for n : names*/name/*# n_index */ = /*n*/'a' /*%if n_has_next */or /*%end*//*%end*/";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
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

  public void testValidate_ifEnd() throws Exception {
    var parser = new SqlParser("select * from aaa /*%if true*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  public void testValidate_ifEnd_selectClause() throws Exception {
    var parser = new SqlParser("select /*%if true*/* from aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  public void testValidate_ifEnd_subquery() throws Exception {
    var parser = new SqlParser("select *, (select /*%if true */ from aaa) x from aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2133, expected.getMessageResource());
    }
  }

  public void testValidate_forEnd() throws Exception {
    var parser = new SqlParser("select * from aaa /*%for name : names*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2134, expected.getMessageResource());
    }
  }

  public void testValidate_unclosedParens() throws Exception {
    var parser = new SqlParser("select * from (select * from bbb");
    try {
      parser.parse();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2135, expected.getMessageResource());
    }
  }

  public void testValidate_enclosedParensByIfBlock() throws Exception {
    var parser = new SqlParser("select * from /*%if true*/(select * from bbb)/*%end*/");
    parser.parse();
  }

  public void testParens_removeAnd() throws Exception {
    var evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, null));
    var testSql =
        "select * from aaa where (\n/*%if name != null*/bbb = /*name*/'ccc'\n/*%else*/\nand ddd is null\n /*%end*/)";
    var parser = new SqlParser(testSql);
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where (\n\n ddd is null\n )", sql.getRawSql());
    assertEquals("select * from aaa where (\n\n ddd is null\n )", sql.getFormattedSql());
    assertEquals(0, sql.getParameters().size());
  }

  public void testEmptyParens() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var parser = new SqlParser("select rank()");
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select rank()", sql.getRawSql());
  }

  public void testEmptyParens_whiteSpace() throws Exception {
    var evaluator = new ExpressionEvaluator();
    var parser = new SqlParser("select rank(   )");
    var sqlNode = parser.parse();
    var sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select rank(   )", sql.getRawSql());
  }

  public void testManyEol() throws Exception {
    var path = "META-INF/" + getClass().getName().replace('.', '/') + "/manyEol.sql";
    var sql = ResourceUtil.getResourceAsString(path);
    var parser = new SqlParser(sql);
    var sqlNode = parser.parse();
    assertNotNull(sqlNode);
  }

  public enum MyEnum {
    AAA,
    BBB,
    CCC
  }
}
