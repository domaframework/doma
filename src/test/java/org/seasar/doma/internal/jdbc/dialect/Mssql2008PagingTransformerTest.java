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

public class Mssql2008PagingTransformerTest {

  @Test
  public void testOffsetLimit() throws Exception {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    Mssql2008PagingTransformer transformer = new Mssql2008PagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly() throws Exception {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5";
    Mssql2008PagingTransformer transformer = new Mssql2008PagingTransformer(5, -1);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testLimitOnly() throws Exception {
    String expected = "select top (10) emp.id from emp order by emp.id";
    Mssql2008PagingTransformer transformer = new Mssql2008PagingTransformer(-1, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
