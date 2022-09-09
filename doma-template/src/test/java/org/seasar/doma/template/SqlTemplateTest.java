package org.seasar.doma.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.dialect.MssqlDialect;

class SqlTemplateTest {

  @Test
  void add() {
    String sql = "select * from emp where id = /* id */1";
    SqlTemplate template = new SqlTemplate(sql);
    SqlTemplate result = template.add("id", Integer.class, 1);
    assertEquals(template, result);
  }

  @Test
  void execute() {
    String sql = "select * from emp where name = /* name */'' and salary = /* salary */0";
    SqlStatement statement =
        new SqlTemplate(sql)
            .add("name", String.class, "abc")
            .add("salary", int.class, 1234)
            .execute();
    assertEquals("select * from emp where name = ? and salary = ?", statement.getRawSql());
    assertEquals(
        "select * from emp where name = 'abc' and salary = 1234", statement.getFormattedSql());
    List<SqlArgument> arguments = statement.getArguments();
    assertEquals(2, arguments.size());
    Iterator<SqlArgument> iterator = arguments.iterator();
    SqlArgument argument1 = iterator.next();
    SqlArgument argument2 = iterator.next();
    assertEquals(String.class, argument1.getType());
    assertEquals("abc", argument1.getValue());
    assertEquals(Integer.class, argument2.getType());
    assertEquals(1234, argument2.getValue());
  }

  @Test
  void defaultDialect() {
    String sql = "select * from emp where name like /* @prefix(name) */'' escape '$'";
    SqlStatement statement = new SqlTemplate(sql).add("name", String.class, "a[b]%c").execute();
    assertEquals("select * from emp where name like ? escape '$'", statement.getRawSql());
    assertEquals(
        "select * from emp where name like 'a[b]$%c%' escape '$'", statement.getFormattedSql());
    List<SqlArgument> arguments = statement.getArguments();
    assertEquals(1, arguments.size());
    Iterator<SqlArgument> iterator = arguments.iterator();
    SqlArgument argument1 = iterator.next();
    assertEquals(String.class, argument1.getType());
    assertEquals("a[b]$%c%", argument1.getValue());
  }

  @Test
  void msSqlDialect() {
    String sql = "select * from emp where name like /* @prefix(name) */'' escape '$'";
    SqlStatement statement =
        new SqlTemplate(sql, new MssqlDialect()).add("name", String.class, "a[b]%c").execute();
    assertEquals("select * from emp where name like ? escape '$'", statement.getRawSql());
    assertEquals(
        "select * from emp where name like 'a$[b]$%c%' escape '$'", statement.getFormattedSql());
    List<SqlArgument> arguments = statement.getArguments();
    assertEquals(1, arguments.size());
    Iterator<SqlArgument> iterator = arguments.iterator();
    SqlArgument argument1 = iterator.next();
    assertEquals(String.class, argument1.getType());
    assertEquals("a$[b]$%c%", argument1.getValue());
  }

  @Test
  void expandDirective() {
    String sql =
        "select /*%expand name */* from emp where name = /* name */'' and salary = /* salary */0";
    try {
      new SqlTemplate(sql)
          .add("name", String.class, "abc")
          .add("salary", int.class, 1234)
          .execute();
    } catch (UnsupportedOperationException e) {
      assertEquals("The '%expand' directive is not supported.", e.getMessage());
    }
  }

  @Test
  void populateDirective() {
    String sql = "update employee set /*%populate*/ id = id where age < 30";
    try {
      new SqlTemplate(sql).execute();
    } catch (UnsupportedOperationException e) {
      assertEquals("The '%populate' directive is not supported.", e.getMessage());
    }
  }
}
