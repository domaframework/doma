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

public class MysqlPagingTransformerTest {

  @Test
  public void testOffsetLimit() throws Exception {
    String expected = "select * from emp order by emp.id limit 5, 10";
    MysqlPagingTransformer transformer = new MysqlPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetLimit_forUpdate() throws Exception {
    String expected = "select * from emp order by emp.id  limit 5, 10 for update";
    MysqlPagingTransformer transformer = new MysqlPagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id for update");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly() throws Exception {
    String expected = "select * from emp order by emp.id limit 5, 18446744073709551615";
    MysqlPagingTransformer transformer = new MysqlPagingTransformer(5, -1);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testLimitOnly() throws Exception {
    String expected = "select * from emp order by emp.id limit 0, 10";
    MysqlPagingTransformer transformer = new MysqlPagingTransformer(-1, 10);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
