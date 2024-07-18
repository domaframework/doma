package org.seasar.doma.ut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

public class CommentRemovalContextTest {

  @Test
  public void removeBlockComments() {
    final MockConfig config =
        new MockConfig() {
          @Override
          public SqlBuilderSettings getSqlBuilderSettings() {
            return new SqlBuilderSettings() {

              @Override
              public boolean shouldRemoveBlankLines() {
                return true;
              }

              @Override
              public boolean shouldRemoveBlockComment(String comment) {
                return true;
              }
            };
          }
        };

    var template =
        """
        /**
        This is block comment 1
        */
        select -- this is line comment 1
          *
        from -- this is lien comment 2
          /**
          This is block comment 2
          */
          EMPLOYEE -- this is line comment 3
        """;
    var expected =
        """
        select -- this is line comment 1
          *
        from -- this is lien comment 2
          EMPLOYEE -- this is line comment 3""";
    var sql = parse(config, template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void removeLineComments() {
    final MockConfig config =
        new MockConfig() {
          @Override
          public SqlBuilderSettings getSqlBuilderSettings() {
            return new SqlBuilderSettings() {

              @Override
              public boolean shouldRemoveBlankLines() {
                return true;
              }

              @Override
              public boolean shouldRemoveLineComment(String comment) {
                return true;
              }
            };
          }
        };

    var template =
        """
        /**
        This is block comment 1
        */
        select -- line comment 1
          *
        from -- line comment 2
          /**
          This is block comment 2
          */
          EMPLOYEE -- line comment 3
        """;
    var expected =
        """
        /**
        This is block comment 1
        */
        select\040
          *
        from\040
          /**
          This is block comment 2
          */
          EMPLOYEE""";
    var sql = parse(config, template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  @Test
  public void removeAllComments() {
    final MockConfig config =
        new MockConfig() {
          @Override
          public SqlBuilderSettings getSqlBuilderSettings() {
            return new SqlBuilderSettings() {

              @Override
              public boolean shouldRemoveBlankLines() {
                return true;
              }

              @Override
              public boolean shouldRemoveBlockComment(String comment) {
                return true;
              }

              @Override
              public boolean shouldRemoveLineComment(String comment) {
                return true;
              }
            };
          }
        };

    var template =
        """
        /**
        This is block comment 1
        */
        select -- line comment 1
          *
        from -- line comment 2
          /**
          This is block comment 2
          */
          EMPLOYEE -- line comment 3
        """;
    var expected = """
        select\040
          *
        from
          EMPLOYEE""";
    var sql = parse(config, template);
    assertEquals(expected, sql.getRawSql());
    assertEquals(expected, sql.getFormattedSql());
  }

  private PreparedSql parse(Config config, String template) {
    return parse(config, template, Collections.emptyMap());
  }

  private PreparedSql parse(Config config, String template, Map<String, Value> map) {
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
