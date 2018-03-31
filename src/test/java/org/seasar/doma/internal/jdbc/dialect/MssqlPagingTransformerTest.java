package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlKind;

/** @author shinsuke-oda */
public class MssqlPagingTransformerTest extends TestCase {

  public void testOffsetLimit() throws Exception {
    var expected = "select emp.id from emp order by emp.id offset 5 rows fetch next 10 rows only";
    var transformer = new MssqlPagingTransformer(5, 10, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetLimit_forceOffsetFetch() throws Exception {
    var expected = "select emp.id from emp order by emp.id offset 5 rows fetch next 10 rows only";
    var transformer = new MssqlPagingTransformer(5, 10, true);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetLimit_option() throws Exception {
    var expected =
        "select emp.id from emp order by emp.id  offset 5 rows fetch next 10 rows only option (maxrecursion 0)";
    var transformer = new MssqlPagingTransformer(5, 10, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly() throws Exception {
    var expected = "select emp.id from emp order by emp.id offset 5 rows";
    var transformer = new MssqlPagingTransformer(5, -1, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly_forceOffsetFetch() throws Exception {
    var expected = "select emp.id from emp order by emp.id offset 5 rows";
    var transformer = new MssqlPagingTransformer(5, -1, true);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly_option() throws Exception {
    var expected = "select emp.id from emp order by emp.id  offset 5 rows option (maxrecursion 0)";
    var transformer = new MssqlPagingTransformer(5, -1, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly() throws Exception {
    var expected = "select top (10) emp.id from emp order by emp.id";
    var transformer = new MssqlPagingTransformer(-1, 10, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_forceOffsetFetch() throws Exception {
    var expected = "select emp.id from emp order by emp.id offset 0 rows fetch next 10 rows only";
    var transformer = new MssqlPagingTransformer(-1, 10, true);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_option() throws Exception {
    var expected = "select top (10) emp.id from emp order by emp.id option (maxrecursion 0)";
    var transformer = new MssqlPagingTransformer(-1, 10, false);
    var parser = new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly_option_forceOffsetFetch() throws Exception {
    var expected =
        "select emp.id from emp order by emp.id  offset 0 rows fetch next 10 rows only option (maxrecursion 0)";
    var transformer = new MssqlPagingTransformer(-1, 10, true);
    var parser = new SqlParser("select emp.id from emp order by emp.id option (maxrecursion 0)");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
