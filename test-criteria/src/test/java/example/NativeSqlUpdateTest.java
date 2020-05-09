package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class NativeSqlUpdateTest {

  private final NativeSql nativeSql;

  public NativeSqlUpdateTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt =
        nativeSql
            .update(e)
            .set(c -> c.value(e.departmentId, 3))
            .where(
                c -> {
                  c.isNotNull(e.managerId);
                  c.ge(e.salary, new Salary("2000"));
                });

    int count = stmt.execute();
    assertEquals(5, count);
  }

  @Test
  void where_empty() {
    Employee_ e = new Employee_();

    EmptyWhereClauseException ex =
        assertThrows(
            EmptyWhereClauseException.class,
            () -> nativeSql.update(e).set(c -> c.value(e.departmentId, 3)).execute());
    System.out.println(ex.getMessage());
  }

  @Test
  void where_empty_allowEmptyWhere_enabled() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt =
        nativeSql
            .update(e, settings -> settings.setAllowEmptyWhere(true))
            .set(c -> c.value(e.departmentId, 3));

    int count = stmt.execute();
    assertEquals(14, count);
  }
}
