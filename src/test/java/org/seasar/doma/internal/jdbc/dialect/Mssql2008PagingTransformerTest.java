package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlKind;

public class Mssql2008PagingTransformerTest extends TestCase {

  public void testOffsetLimit() throws Exception {
    var expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    var transformer = new Mssql2008PagingTransformer(5, 10);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly() throws Exception {
    var expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5";
    var transformer = new Mssql2008PagingTransformer(5, -1);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly() throws Exception {
    var expected = "select top (10) emp.id from emp order by emp.id";
    var transformer = new Mssql2008PagingTransformer(-1, 10);
    var parser = new SqlParser("select emp.id from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
