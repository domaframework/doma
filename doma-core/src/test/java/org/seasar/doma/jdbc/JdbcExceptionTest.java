package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.message.Message;

public class JdbcExceptionTest {

  private final MockConfig config = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testSqlFileNotFound() throws Exception {
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    try {
      repository.getSqlFile(method, "META-INF/aaa/bbb.sql", new StandardDialect());
      fail();
    } catch (SqlFileNotFoundException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2011, e.getMessageResource());
    }
  }

  @Test
  public void testQuotationNotClosed() throws Exception {
    SqlParser parser = new SqlParser("select * from 'aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2101, e.getMessageResource());
    }
  }

  @Test
  public void testBlockCommentNotClosed() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa /*aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2102, e.getMessageResource());
    }
  }

  @Test
  public void testIfCommentNotFoundForEndComment() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa/*%end*/ ");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2104, e.getMessageResource());
    }
  }

  @Test
  public void testIfCommentNotFoundForSecondEndComment() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where/*%if true*//*%end*/ /*%end*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2104, e.getMessageResource());
    }
  }

  @Test
  public void testOpenedParensNotFound() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where )");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2109, e.getMessageResource());
    }
  }

  @Test
  public void testTestLiteralNotFound() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where bbb = /*bbb*/ 'ccc')");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2110, e.getMessageResource());
    }
  }

  @Test
  public void testSqlBuildingFailed() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where bbb = \n/*bbb*/'ccc'");
    SqlNode sqlNode = parser.parse();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath");
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2111, e.getMessageResource());
    }
  }

  @Test
  public void testBindValueTypeNotIterable() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where bbb in /*bbb*/(1,2,3)");
    SqlNode sqlNode = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("bbb", new Value(int.class, 1));
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2112, e.getMessageResource());
    }
  }

  @Test
  public void testCollectionOfBindValueContainsNull() throws Exception {
    SqlParser parser = new SqlParser("select * from aaa where bbb in /*bbb*/(1,2,3)");
    SqlNode sqlNode = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("bbb", new Value(List.class, Arrays.asList(1, null)));
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2115, e.getMessageResource());
    }
  }
}
