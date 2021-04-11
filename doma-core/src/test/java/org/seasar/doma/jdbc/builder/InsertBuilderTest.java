package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;

public class InsertBuilderTest {

  @Test
  public void test() {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");
    builder.execute();
  }

  @Test
  public void testGetSql() {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.param(String.class, "SMITH").sql(", ");
    builder.param(int.class, 100).sql(")");

    String sql = String.format("insert into Emp%n" + "(name, salary)%n" + "values (?, ?)");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testLiteral() {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into Emp");
    builder.sql("(name, salary)");
    builder.sql("values (");
    builder.literal(String.class, "SMITH").sql(", ");
    builder.literal(int.class, 100).sql(")");

    String sql = String.format("insert into Emp%n" + "(name, salary)%n" + "values ('SMITH', 100)");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testParams() throws Exception {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into TmpEmp");
    builder.sql("(name, salary)");
    builder.sql("select name, salary from Emp");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "insert into TmpEmp%n"
                + "(name, salary)%n"
                + "select name, salary from Emp%n"
                + "where%n"
                + "name in (?, ?, ?)%n"
                + "and%n"
                + "salary in (?, ?, ?)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(6, params.size());
    assertEquals("x", params.get(0).getValue());
    assertEquals("y", params.get(1).getValue());
    assertEquals("z", params.get(2).getValue());
    assertEquals(10, params.get(3).getValue());
    assertEquals(20, params.get(4).getValue());
    assertEquals(30, params.get(5).getValue());
  }

  @Test
  public void testParams_empty() throws Exception {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into TmpEmp");
    builder.sql("(name, salary)");
    builder.sql("select name, salary from Emp");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
            String.format(
                "insert into TmpEmp%n"
                    + "(name, salary)%n"
                    + "select name, salary from Emp%n"
                    + "where%n"
                    + "name in (null)%n"
                    + "and%n"
                    + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  @Test
  public void testLiterals() throws Exception {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into TmpEmp");
    builder.sql("(name, salary)");
    builder.sql("select name, salary from Emp");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
            String.format(
                "insert into TmpEmp%n"
                    + "(name, salary)%n"
                    + "select name, salary from Emp%n"
                    + "where%n"
                    + "name in ('x', 'y', 'z')%n"
                    + "and%n"
                    + "salary in (10, 20, 30)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  @Test
  public void testLiterals_empty() throws Exception {
    InsertBuilder builder = InsertBuilder.newInstance(new MockConfig());
    builder.sql("insert into TmpEmp");
    builder.sql("(name, salary)");
    builder.sql("select name, salary from Emp");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
            String.format(
                "insert into TmpEmp%n"
                    + "(name, salary)%n"
                    + "select name, salary from Emp%n"
                    + "where%n"
                    + "name in (null)%n"
                    + "and%n"
                    + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }
}
