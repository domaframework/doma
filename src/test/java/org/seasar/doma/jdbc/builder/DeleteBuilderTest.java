package org.seasar.doma.jdbc.builder;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class DeleteBuilderTest extends TestCase {

  public void test() throws Exception {
    var builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").param(int.class, 10);
    builder.execute();
  }

  public void testGetSql() throws Exception {
    var builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").param(int.class, 10);

    var sql =
        String.format("delete from Emp%n" + "where%n" + "name = ?%n" + "and%n" + "salary = ?");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  public void testLiterall() throws Exception {
    var builder = DeleteBuilder.newInstance(new MockConfig());
    builder.sql("delete from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "aaa");
    builder.sql("and");
    builder.sql("salary = ").literal(int.class, 10);

    var sql =
        String.format("delete from Emp%n" + "where%n" + "name = 'aaa'%n" + "and%n" + "salary = 10");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }
}
