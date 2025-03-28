/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class JdbcExceptionTest {

  private final MockConfig config = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testSqlFileNotFound() {
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
  public void testQuotationNotClosed() {
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
  public void testBlockCommentNotClosed() {
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
  public void testIfCommentNotFoundForEndComment() {
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
  public void testIfCommentNotFoundForSecondEndComment() {
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
  public void testOpenedParensNotFound() {
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
  public void testTestLiteralNotFound() {
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
  public void testSqlBuildingFailed() {
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
  public void testBindValueTypeNotIterable() {
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
  public void testCollectionOfBindValueContainsNull() {
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
