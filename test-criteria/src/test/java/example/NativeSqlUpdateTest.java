package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.add;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;

@ExtendWith(Env.class)
public class NativeSqlUpdateTest {

  private final NativeSql nativeSql;

  public NativeSqlUpdateTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(
                e,
                settings -> {
                  settings.setComment("update all");
                  settings.setQueryTimeout(1000);
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setAllowEmptyWhere(true);
                  settings.setBatchSize(20);
                })
            .set(
                c -> {
                  c.value(e.employeeName, "aaa");
                })
            .execute();

    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.departmentId, 3))
            .where(
                c -> {
                  c.isNotNull(e.managerId);
                  c.ge(e.salary, new Salary("2000"));
                })
            .execute();

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

    int count =
        nativeSql
            .update(e, settings -> settings.setAllowEmptyWhere(true))
            .set(c -> c.value(e.departmentId, 3))
            .execute();

    assertEquals(14, count);
  }

  @Test
  void expression_add() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.version, add(e.version, 10)))
            .where(c -> c.eq(e.employeeId, 1))
            .execute();

    assertEquals(1, count);

    Employee employee = nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOne();
    assertNotNull(employee);
    assertEquals(11, employee.getVersion());
  }
}
