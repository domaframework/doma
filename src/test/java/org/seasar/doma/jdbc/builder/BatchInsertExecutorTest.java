package org.seasar.doma.jdbc.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.query.SqlBatchInsertQuery;
import org.seasar.doma.message.Message;

/** @author bakenezumi */
public class BatchInsertExecutorTest extends TestCase {

  private SqlBatchInsertQuery mockQuery() {
    final var query = new SqlBatchInsertQuery();
    query.setConfig(new MockConfig());
    query.setSqlLogType(SqlLogType.FORMATTED);
    return query;
  }

  public void testBuilder() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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

  public void testGetSql() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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
    var sql = String.format("insert into Emp%n" + "(name, salary)%n" + "values (?, ?)");

    var sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    var sql0 = sqls.get(0);
    assertEquals(sql, sql0.getRawSql());
    var parameters0 = sql0.getParameters();
    assertEquals(2, parameters0.size());
    assertEquals("SMITH", parameters0.get(0).getValue());
    assertEquals(100, parameters0.get(1).getValue());

    var sql1 = sqls.get(1);
    assertEquals(sql, sql1.getRawSql());
    var parameters1 = sql1.getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertEquals(200, parameters1.get(1).getValue());
  }

  public void testLiteral() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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

    var sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    var sql0 = sqls.get(0);
    assertEquals(
        String.format("insert into Emp%n" + "(name, salary)%n" + "values ('SMITH', ?)"),
        sql0.getRawSql());
    var parameters0 = sql0.getParameters();
    assertEquals(1, parameters0.size());
    assertEquals(100, parameters0.get(0).getValue());

    var sql1 = sqls.get(1);
    assertEquals(
        String.format("insert into Emp%n" + "(name, salary)%n" + "values ('ALLEN', ?)"),
        sql1.getRawSql());
    var parameters1 = sql1.getParameters();
    assertEquals(1, parameters1.size());
    assertEquals(200, parameters1.get(0).getValue());
  }

  public void testNotEqualParamCall() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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

  public void testChangeType() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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

  public void testParamToLiteral() throws Exception {
    var query = mockQuery();
    var builder = BatchBuilder.newInstance(query);
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

  public void testExecutor() throws Exception {
    var employees =
        Arrays.asList(new Employee[] {new Employee("SMITH", 100), new Employee("ALLEN", 200)});
    var executor = BatchInsertExecutor.newInstance(new MockConfig());
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
    var executor = BatchInsertExecutor.newInstance(new MockConfig());
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
