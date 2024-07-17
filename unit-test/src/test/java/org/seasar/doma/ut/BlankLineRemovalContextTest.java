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
import org.seasar.doma.jdbc.SqlParserConfig;

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
  private final SqlParserConfig sqlParserConfig =
      new SqlParserConfig() {

        @Override
        public boolean shouldRemoveBlockComment(String comment) {
          return false;
        }

        @Override
        public boolean shouldRemoveLineComment(String comment) {
          return false;
        }
      };

  @Test
  public void testIfOneLine() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa
          ccc
          ddd""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testIf_and() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa = 1
          and
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testIf_or() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa = 1
          or
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testElseif_and() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          bbb = 2
          and
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testElseif_or() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          bbb = 2
          or
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testElse_and() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ccc = 3
          and
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testElse_or() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ccc = 3
          or
          ddd = 4""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testNestedIf_true_true() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          aaa
          ddd""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testNestedIf_true_false() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ddd""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testNestedIf_false() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        where
          ddd""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testFor() {
    Map<String, Value> map = new HashMap<>();
    map.put("list", new Value(List.class, Arrays.asList("a", "b", "c")));
    String template =
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
    String expected =
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
    assertEquals(expected, parse(template, map));
  }

  @Test
  public void testWhere() {
    String template =
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
    String expected = """
        select
          *
        from
          EMPLOYEE""";
    assertEquals(expected, parse(template));
  }

  @Test
  public void testWhere_orderBy() {
    String template =
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
    String expected =
        """
        select
          *
        from
          EMPLOYEE
        order by
          bbb, ccc""";
    assertEquals(expected, parse(template));
  }

  private String parse(String template) {
    return parse(template, Collections.emptyMap());
  }

  private String parse(String template, Map<String, Value> map) {
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            map, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    SqlParser parser = new SqlParser(template, sqlParserConfig);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    return sql.getRawSql();
  }
}
