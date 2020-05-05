package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class NativeSqlUpdateTest {

  private final Config config;

  public NativeSqlUpdateTest(Config config) {
    this.config = config;
  }

  @Test
  void set() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt =
        NativeSql.update(e)
            .set(
                c -> {
                  c.value(e.employeeName, "aaa");
                  c.value(e.salary, new Salary("2000"));
                });

    int count = stmt.execute(config);
    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt =
        NativeSql.update(e)
            .set(c -> c.value(e.departmentId, 3))
            .where(
                c -> {
                  c.isNotNull(e.managerId);
                  c.ge(e.salary, new Salary("2000"));
                });

    int count = stmt.execute(config);
    assertEquals(5, count);

    System.out.println(stmt.asSql(config));
  }
}
