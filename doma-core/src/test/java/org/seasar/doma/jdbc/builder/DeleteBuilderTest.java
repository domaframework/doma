package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;

public class DeleteBuilderTest {

  @Test
  public void test() {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").param(int.class, 10);
    builder.execute();
  }

  @Test
  public void testGetSql() {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").param(int.class, 10);

    String sql =
        String.format("delete from Emp%n" + "where%n" + "name = ?%n" + "and%n" + "salary = ?");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testLiteral() {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").literal(int.class, 10);

    String sql =
        String.format("delete from Emp%n" + "where%n" + "name = 'aaa'%n" + "and%n" + "salary = 10");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testParams() throws Exception {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "delete from Emp%n"
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
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "delete from Emp%n" + "where%n" + "name in (null)%n" + "and%n" + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  @Test
  public void testLiterals() throws Exception {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "delete from Emp%n"
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
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "delete from Emp%n" + "where%n" + "name in (null)%n" + "and%n" + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }
}
