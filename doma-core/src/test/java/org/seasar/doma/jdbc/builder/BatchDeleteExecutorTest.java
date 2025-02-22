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
package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.query.SqlBatchDeleteQuery;
import org.seasar.doma.message.Message;

/**
 * @author bakenezumi
 */
public class BatchDeleteExecutorTest {

  private SqlBatchDeleteQuery mockQuery() {
    final SqlBatchDeleteQuery query = new SqlBatchDeleteQuery();
    query.setConfig(new MockConfig());
    query.setSqlLogType(SqlLogType.FORMATTED);
    return query;
  }

  @Test
  public void testBuilder() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "ALLEN");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("2000"));
    builder = builder.fixSql();

    builder.execute(() -> new BatchDeleteCommand(query));
  }

  @Test
  public void testGetSql() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "ALLEN");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("2000"));
    builder = builder.fixSql();

    String sql =
        String.format("delete from Emp%n" + "where%n" + "name = ?%n" + "and%n" + "salary = ?");

    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    Sql<?> sql0 = sqls.get(0);
    assertEquals(sql, sql0.getRawSql());
    List<? extends SqlParameter> parameters0 = sql0.getParameters();
    assertEquals(2, parameters0.size());
    assertEquals("SMITH", parameters0.get(0).getValue());
    assertEquals(new BigDecimal("1000"), parameters0.get(1).getValue());

    Sql<?> sql1 = sqls.get(1);
    assertEquals(sql, sql1.getRawSql());
    List<? extends SqlParameter> parameters1 = sql1.getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertEquals(new BigDecimal("2000"), parameters1.get(1).getValue());
  }

  @Test
  public void testLiteral() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").literal(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "ALLEN");
    builder.sql("and");
    builder.sql("salary = ").literal(BigDecimal.class, new BigDecimal("2000"));
    builder = builder.fixSql();

    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    Sql<?> sql0 = sqls.get(0);
    assertEquals(
        String.format(
            "delete from Emp%n" + "where%n" + "name = 'SMITH'%n" + "and%n" + "salary = 1000"),
        sql0.getRawSql());
    List<? extends SqlParameter> parameters0 = sql0.getParameters();
    assertEquals(0, parameters0.size());

    Sql<?> sql1 = sqls.get(1);
    assertEquals(
        String.format(
            "delete from Emp%n" + "where%n" + "name = 'ALLEN'%n" + "and%n" + "salary = 2000"),
        sql1.getRawSql());
    List<? extends SqlParameter> parameters1 = sql1.getParameters();
    assertEquals(0, parameters1.size());
  }

  @Test
  public void testNotEqualParamCall() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "ALLEN");
    builder = builder.fixSql();

    try {
      builder.execute(() -> new BatchDeleteCommand(query));
    } catch (AssertionError e) {
      return;
    }

    fail();
  }

  @Test
  public void testChangeType() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "ALLEN");
    builder.sql("and");
    builder = builder.sql("salary = ");

    try {
      builder.param(int.class, 2000);
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2229, e.getMessageResource());
      return;
    }

    fail();
  }

  @Test
  public void testParamToLiteral() {
    SqlBatchDeleteQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "SMITH");
    builder.sql("and");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder = builder.fixSql();
    builder.sql("delete from Emp");
    builder.sql("where");
    builder = builder.sql("name = ");

    try {
      builder.literal(String.class, "ALLEN");
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2230, e.getMessageResource());
      return;
    }

    fail();
  }

  private static class Employee {
    final int id;

    final String name;
    final BigDecimal salary;

    Employee(int id, String name, BigDecimal salary) {
      this.id = id;
      this.name = name;
      this.salary = salary;
    }
  }

  @Test
  public void testExecutor() {
    List<Employee> employees =
        Arrays.asList(
            new Employee(10, "SMITH", new BigDecimal("1001")),
            new Employee(20, "ALLEN", new BigDecimal("2001")));
    BatchDeleteExecutor executor = BatchDeleteExecutor.newInstance(new MockConfig());
    executor.execute(
        employees,
        (emp, builder) -> {
          builder.sql("delete from Emp");
          builder.sql("where");
          builder.sql("name = ").param(String.class, emp.name);
          builder.sql("and");
          builder.sql("salary = ").param(BigDecimal.class, emp.salary);
        });
  }
}
