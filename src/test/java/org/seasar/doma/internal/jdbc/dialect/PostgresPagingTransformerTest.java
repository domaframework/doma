package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlKind;

public class PostgresPagingTransformerTest extends TestCase {

  public void testOffsetLimit() throws Exception {
    var expected = "select * from emp order by emp.id limit 10 offset 5";
    var transformer = new PostgresPagingTransformer(5, 10);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetLimit_forUpdate() throws Exception {
    var expected = "select * from emp order by emp.id  limit 10 offset 5 for update";
    var transformer = new PostgresPagingTransformer(5, 10);
    var parser = new SqlParser("select * from emp order by emp.id for update");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly() throws Exception {
    var expected = "select * from emp order by emp.id offset 5";
    var transformer = new PostgresPagingTransformer(5, -1);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly() throws Exception {
    var expected = "select * from emp order by emp.id limit 10";
    var transformer = new PostgresPagingTransformer(-1, 10);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
