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
package org.seasar.doma.template;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.message.Message;

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

  @Test
  void validate() {
    // The values are not needed, because the expressions are not evaluated
    String sql = "select * from emp where\n/*%if name != null */\n  name = /* name */'a'\n/*%end*/";
    assertDoesNotThrow(() -> new SqlTemplate(sql).validate());
  }

  @Test
  void validate_syntaxError() {
    String sql = "select * from emp where\n/*%if name != null */\n  name = /* name */'a'";
    SqlTemplateSyntaxException e =
        assertThrows(SqlTemplateSyntaxException.class, () -> new SqlTemplate(sql).validate());
    assertEquals(Message.DOMA2133, e.getMessageResource());
    assertEquals(sql, e.getSql());
    assertEquals(2, e.getLineNumber());
    assertEquals(21, e.getColumnNumber());
  }

  @Test
  void validate_syntaxError_keepsMessageAndCause() {
    String sql = "select * from emp where\n/*%if name != null */\n  name = /* name */'a'";
    SqlTemplateSyntaxException e =
        assertThrows(SqlTemplateSyntaxException.class, () -> new SqlTemplate(sql).validate());
    JdbcException cause = assertInstanceOf(JdbcException.class, e.getCause());
    assertEquals(cause.getMessage(), e.getMessage());
    assertArrayEquals((Object[]) cause.getArgs(), (Object[]) e.getArgs());
  }

  @Test
  void validate_unclosedComment() {
    String sql = "select 1\nfrom T\n/*%end\ngroup by\n  A";
    SqlTemplateSyntaxException e =
        assertThrows(SqlTemplateSyntaxException.class, () -> new SqlTemplate(sql).validate());
    assertEquals(Message.DOMA2102, e.getMessageResource());
    assertEquals(3, e.getLineNumber());
  }

  @Test
  void validate_everySyntaxErrorIsConverted() {
    // Errors from both the tokenizer and the parser
    String[] sqls = {
      "select 'abc", // DOMA2101
      "select /*%xxx*/", // DOMA2119
      "select * from emp /*%end*/", // DOMA2104
      "select * from emp)", // DOMA2109
      "select * from emp where id = /* id */", // DOMA2110
      "select * from emp /*%for e : list */", // DOMA2134
      "select * from emp /*%else*/", // DOMA2140
    };
    for (String sql : sqls) {
      SqlTemplateSyntaxException e =
          assertThrows(
              SqlTemplateSyntaxException.class, () -> new SqlTemplate(sql).validate(), sql);
      assertEquals(1, e.getLineNumber(), sql);
    }
  }
}
