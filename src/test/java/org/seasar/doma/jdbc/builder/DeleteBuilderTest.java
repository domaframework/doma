package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class DeleteBuilderTest {

  @Test
  public void test() throws Exception {
    DeleteBuilder builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").param(int.class, 10);
    builder.execute();
  }

  @Test
  public void testGetSql() throws Exception {
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
  public void testLiterall() throws Exception {
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
}
