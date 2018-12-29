package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class StandardPagingTransformerTest extends TestCase {

  public void testOffsetLimit() throws Exception {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    StandardPagingTransformer transformer = new StandardPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOffsetOnly() throws Exception {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5";
    StandardPagingTransformer transformer = new StandardPagingTransformer(5, -1);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testLimitOnly() throws Exception {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ <= 10";
    StandardPagingTransformer transformer = new StandardPagingTransformer(-1, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testOrderByClauseUnspecified() throws Exception {
    StandardPagingTransformer transformer = new StandardPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select * from emp");
    try {
      transformer.transform(parser.parse());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2201, expected.getMessageResource());
    }
  }
}
