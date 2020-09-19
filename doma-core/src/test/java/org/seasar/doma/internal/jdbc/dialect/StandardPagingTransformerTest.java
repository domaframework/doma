package org.seasar.doma.internal.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.message.Message;

public class StandardPagingTransformerTest {

  @Test
  public void testOffsetLimit() {
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

  @Test
  public void testOffsetLimit_ifNode() {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.name desc, temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    StandardPagingTransformer transformer = new StandardPagingTransformer(5, 10);
    SqlParser parser =
        new SqlParser("select emp.id from emp order by /*%if true*/emp.name desc,/*%end*/ emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetLimit_forNode() {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.name1, temp_.name2, temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    StandardPagingTransformer transformer = new StandardPagingTransformer(5, 10);
    SqlParser parser =
        new SqlParser(
            "select emp.id from emp order by /*%for e: values*/emp.name/*#e*/, /*%end*/emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("values", new Value(List.class, Arrays.asList(1, 2)));
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            new MockConfig(), SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly() {
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

  @Test
  public void testLimitOnly() {
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

  @Test
  public void testOrderByClauseUnspecified() {
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
