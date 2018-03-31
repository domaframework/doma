package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;

public class Mssql2008ForUpdateTransformerTest extends TestCase {

  public void testForUpdateNormal() throws Exception {
    var expected = "select * from emp with (updlock, rowlock) order by emp.id";
    var transformer = new Mssql2008ForUpdateTransformer(SelectForUpdateType.NORMAL, 0);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testForUpdateNormal_originalSqlNodeUnchanged() throws Exception {
    var original = "select * from emp order by emp.id";
    var transformer = new Mssql2008ForUpdateTransformer(SelectForUpdateType.NORMAL, 0);
    var parser = new SqlParser(original);
    var originalSqlNode = parser.parse();
    var sqlNode = transformer.transform(originalSqlNode);
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    sqlBuilder.build(sqlNode, Function.identity());
    sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(originalSqlNode, Function.identity());
    assertEquals(original, sql.getRawSql());
  }

  public void testForUpdateNowait() throws Exception {
    var expected = "select * from emp with (updlock, rowlock, nowait) order by emp.id";
    var transformer = new Mssql2008ForUpdateTransformer(SelectForUpdateType.NOWAIT, 0);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
