package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.InsertStatement;

class NativeSqlInsertTest {

  private final Config config = new MockConfig();

  @Test
  void insertInto() {
    Emp_ e = new Emp_();
    InsertStatement stmt =
        NativeSql.insert
            .into(e)
            .values(
                c -> {
                  c.value(e.id, 99);
                  c.value(e.name, "aaa");
                  c.value(e.salary, null);
                  c.value(e.version, 1);
                });

    Sql<?> sql = stmt.asSql(config);
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1)",
        sql.getFormattedSql());
  }
}
