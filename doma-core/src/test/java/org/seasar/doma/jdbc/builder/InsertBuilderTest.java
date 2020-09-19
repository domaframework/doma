package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

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
}
