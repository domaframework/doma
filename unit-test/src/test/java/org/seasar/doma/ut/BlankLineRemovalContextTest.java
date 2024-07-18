package org.seasar.doma.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

public class BlankLineRemovalContextTest {

  private final MockConfig config =
      new MockConfig() {
        @Override
        public SqlBuilderSettings getSqlBuilderSettings() {
          return new SqlBuilderSettings() {
            @Override
            public boolean shouldRemoveBlankLines() {
              // use NodePreparedSqlBuilder.BlankLineRemovalContext
              return true;
            }
          };
        }
      };

  @Test
  public void testIfOneLine() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if true */aaa/*%end */
          /*%if false */bbb/*%end */
          /*%if true */ccc/*%end */
          ddd""";
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa
          ccc
          ddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testIf_and() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if true */
          aaa = 1
          /*%elseif false */
          and
          bbb = 2
          /*%else */
          and
          ccc = 3
          /*%end */
          and
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa = 1
          and
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testIf_or() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if true */
          aaa = 1
          /*%elseif false */
          or
          bbb = 2
          /*%else */
          or
          ccc = 3
          /*%end */
          or
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa = 1
          or
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testElseif_and() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%elseif true */
          and
          bbb = 2
          /*%else */
          and
          ccc = 3
          /*%end */
          and
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          bbb = 2
          and
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testElseif_or() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%elseif true */
          or
          bbb = 2
          /*%else */
          or
          ccc = 3
          /*%end */
          or
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          bbb = 2
          or
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testElse_and() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%elseif false */
          and
          bbb = 2
          /*%else */
          and
          ccc = 3
          /*%end */
          and
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ccc = 3
          and
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testMultipleBlankLines() {
    var template =
        """
        aaa

          bbb


            ccc



              ddd
        """;
    var expected = """
        aaa
          bbb
            ccc
              ddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testTabs() {
    var template =
        """
        aaa\t\t\t\t

        \tbbb\t\t\t


        \t\tccc\t\t



        \t\t\tddd\t
        """;
    var expected = """
        aaa
        \tbbb
        \t\tccc
        \t\t\tddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testQuotes() {
    var template = """
        'aaa

          bbb', '

            ccc

        '
        """;
    var expected = """
        'aaa

          bbb', '

            ccc

        '""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testElse_or() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%elseif false */
          or
          bbb = 2
          /*%else */
          or
          ccc = 3
          /*%end */
          or
          ddd = 4
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ccc = 3
          or
          ddd = 4""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testNestedIf_true_true() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if true */
            /*%if true */
          aaa
            /*%end */
          /*%end */
          ddd
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa
          ddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testNestedIf_true_false() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if true */
            /*%if false */
          aaa
            /*%end */
          /*%end */
          ddd
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testNestedIf_false() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
            /*%if false */
          aaa
            /*%end */
          /*%end */
          ddd
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ddd""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testFor() {
    Map<String, Value> map = new HashMap<>();
    map.put("list", new Value(List.class, Arrays.asList("a", "b", "c")));
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%for item : list */
          name = /* item */''
            /*%if item_has_next */
          /*# "or" */
            /*%end */
          /*%end*/
          ddd
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          name = ?
          or
          name = ?
          or
          name = ?
          ddd""";
    var sql = parse(template, map);
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testWhere() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%end*/
        """;
    var expected = """
        select
          *
        from
          EMPLOYEE""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void testWhere_orderBy() {
    var template =
        """
        select
          *
        from
          EMPLOYEE
        where
          /*%if false */
          aaa = 1
          /*%end*/
        order by
          bbb, ccc
        """;
    var expected =
        """
        select
          *
        from
          EMPLOYEE
        order by
          bbb, ccc""";
    var sql = parse(template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  private PreparedSql parse(String template) {
    return parse(template, Collections.emptyMap());
  }

  private PreparedSql parse(String template, Map<String, Value> map) {
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            map, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    SqlParser parser = new SqlParser(template);
    SqlNode sqlNode = parser.parse();
    return new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
        .build(sqlNode, Function.identity());
  }
}
