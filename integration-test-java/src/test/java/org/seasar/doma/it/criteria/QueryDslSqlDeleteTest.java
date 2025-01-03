package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslSqlDeleteTest {

  private final QueryDsl dsl;

  public QueryDslSqlDeleteTest(Config config) {
    this.dsl = new QueryDsl(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    int count =
        dsl.delete(
                e,
                settings -> {
                  settings.setComment("delete all");
                  settings.setQueryTimeout(1000);
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setAllowEmptyWhere(true);
                  settings.setBatchSize(20);
                })
            .where(__ -> {})
            .execute();

    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    int count = dsl.delete(e).where(c -> c.ge(e.salary, new Salary("2000"))).execute();

    assertEquals(6, count);
  }

  @Test
  void where_empty() {
    Employee_ e = new Employee_();

    EmptyWhereClauseException ex =
        assertThrows(
            EmptyWhereClauseException.class, () -> dsl.delete(e).where(__ -> {}).execute());
    System.out.println(ex.getMessage());
  }

  @Test
  void where_empty_allowEmptyWhere_enabled() {
    Employee_ e = new Employee_();

    int count =
        dsl.delete(e, settings -> settings.setAllowEmptyWhere(true)).where(__ -> {}).execute();

    assertEquals(14, count);
  }

  @Test
  void all() {
    Employee_ e = new Employee_();

    int count = dsl.delete(e).all().execute();

    assertEquals(14, count);
  }
}
