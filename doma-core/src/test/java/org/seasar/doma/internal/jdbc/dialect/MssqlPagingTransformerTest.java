package org.seasar.doma.internal.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author shinsuke-oda
 */
public class MssqlPagingTransformerTest {

  @Test
  public void testOffsetLimit() {
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

  @Test
  public void testOffsetLimit_forceOffsetFetch() {
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

  @Test
  public void testOffsetLimit_option() {
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

  @Test
  public void testOffsetOnly() {
    String expected = "select emp.id from emp order by emp.id offset 5 rows";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, -1, false);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly_forceOffsetFetch() {
    String expected = "select emp.id from emp order by emp.id offset 5 rows";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(5, -1, true);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly_option() {
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

  @Test
  public void testLimitOnly() {
    String expected = "select top (10) emp.id from emp order by emp.id";
    MssqlPagingTransformer transformer = new MssqlPagingTransformer(-1, 10, false);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testLimitOnly_forceOffsetFetch() {
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

  @Test
  public void testLimitOnly_option() {
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

  @Test
  public void testLimitOnly_option_forceOffsetFetch() {
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
