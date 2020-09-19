package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.query.SqlBatchInsertQuery;
import org.seasar.doma.message.Message;

/** @author bakenezumi */
public class BatchInsertExecutorTest {

  private SqlBatchInsertQuery mockQuery() {
    final SqlBatchInsertQuery query = new SqlBatchInsertQuery();
    query.setConfig(new MockConfig());
    query.setSqlLogType(SqlLogType.FORMATTED);
    return query;
  }

  @Test
  public void testBuilder() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "ALLEN").sql(", ");
    builder.param(int.class, 200).sql(")");
    builder = builder.fixSql();

    builder.execute(() -> new BatchInsertCommand(query));
  }

  @Test
  public void testGetSql() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "ALLEN").sql(", ");
    builder.param(int.class, 200).sql(")");
    builder = builder.fixSql();
    String sql = String.format("insert into Emp%n" + "(name, salary)%n" + "values (?, ?)");

    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    Sql<?> sql0 = sqls.get(0);
    assertEquals(sql, sql0.getRawSql());
    List<? extends SqlParameter> parameters0 = sql0.getParameters();
    assertEquals(2, parameters0.size());
    assertEquals("SMITH", parameters0.get(0).getValue());
    assertEquals(100, parameters0.get(1).getValue());

    Sql<?> sql1 = sqls.get(1);
    assertEquals(sql, sql1.getRawSql());
    List<? extends SqlParameter> parameters1 = sql1.getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertEquals(200, parameters1.get(1).getValue());
  }

  @Test
  public void testLiteral() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.literal(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.literal(String.class, "ALLEN").sql(", ");
    builder.param(int.class, 200).sql(")");
    builder = builder.fixSql();

    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    Sql<?> sql0 = sqls.get(0);
    assertEquals(
        String.format("insert into Emp%n" + "(name, salary)%n" + "values ('SMITH', ?)"),
        sql0.getRawSql());
    List<? extends SqlParameter> parameters0 = sql0.getParameters();
    assertEquals(1, parameters0.size());
    assertEquals(100, parameters0.get(0).getValue());

    Sql<?> sql1 = sqls.get(1);
    assertEquals(
        String.format("insert into Emp%n" + "(name, salary)%n" + "values ('ALLEN', ?)"),
        sql1.getRawSql());
    List<? extends SqlParameter> parameters1 = sql1.getParameters();
    assertEquals(1, parameters1.size());
    assertEquals(200, parameters1.get(0).getValue());
  }

  @Test
  public void testNotEqualParamCall() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "ALLEN").sql(")");
    builder = builder.fixSql();

    try {
      builder.execute(() -> new BatchInsertCommand(query));
    } catch (AssertionError e) {
      return;
    }

    fail();
  }

  @Test
  public void testChangeType() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");

    try {
      builder.param(int.class, 10).sql(", ");
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2229, e.getMessageResource());
      return;
    }

    fail();
  }

  @Test
  public void testParamToLiteral() throws Exception {
    SqlBatchInsertQuery query = mockQuery();
    BatchBuilder builder = BatchBuilder.newInstance(query);
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder = builder.fixSql();
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");

    try {
      builder.literal(String.class, "ALLEN").sql(", ");
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2230, e.getMessageResource());
      return;
    }

    fail();
  }

  private static class Employee {
    final String name;
    final int salary;

    Employee(String name, int salary) {
      this.name = name;
      this.salary = salary;
    }
  }

  @Test
  public void testExecutor() throws Exception {
    List<Employee> employees =
        Arrays.asList(new Employee("SMITH", 100), new Employee("ALLEN", 200));
    BatchInsertExecutor executor = BatchInsertExecutor.newInstance(new MockConfig());
    executor.execute(
        employees,
        (emp, builder) -> {
          builder.sql("insert into Emp");
          builder.sql("(name, salary)");
          builder.sql("values (");
          builder.param(String.class, emp.name).sql(", ");
          builder.param(int.class, emp.salary).sql(")");
        });
  }

  @SuppressWarnings("serial")
  @Test
  public void testExecutorByMap() throws Exception {
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                    put("salary", 500);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", null);
                  }
                });
          }
        };
    BatchInsertExecutor executor = BatchInsertExecutor.newInstance(new MockConfig());
    executor.execute(
        employees,
        (emp, builder) -> {
          builder.sql("insert into Emp");
          builder.sql("(name, salary)");
          builder.sql("values (");
          builder.param(String.class, (String) emp.get("name")).sql(", ");
          builder.param(Integer.class, (Integer) emp.get("salary")).sql(")");
        });
  }
}
