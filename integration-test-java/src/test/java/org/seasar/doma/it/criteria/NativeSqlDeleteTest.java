package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;

@ExtendWith(IntegrationTestEnvironment.class)
public class NativeSqlDeleteTest {

  private final NativeSql nativeSql;

  public NativeSqlDeleteTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .delete(
                e,
                settings -> {
                  settings.setComment("delete all");
                  settings.setQueryTimeout(1000);
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setAllowEmptyWhere(true);
                  settings.setBatchSize(20);
                })
            .execute();

    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    int count = nativeSql.delete(e).where(c -> c.ge(e.salary, new Salary("2000"))).execute();

    assertEquals(6, count);
  }

  @Test
  void where_empty() {
    Employee_ e = new Employee_();

    EmptyWhereClauseException ex =
        assertThrows(EmptyWhereClauseException.class, () -> nativeSql.delete(e).execute());
    System.out.println(ex.getMessage());
  }

  @Test
  void where_empty_allowEmptyWhere_enabled() {
    Employee_ e = new Employee_();

    int count = nativeSql.delete(e, settings -> settings.setAllowEmptyWhere(true)).execute();

    assertEquals(14, count);
  }
}
