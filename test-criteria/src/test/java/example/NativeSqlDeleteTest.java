package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class NativeSqlDeleteTest {

  private final Config config;

  public NativeSqlDeleteTest(Config config) {
    this.config = config;
  }

  @Test
  void from() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt = NativeSql.delete.from(e);

    int count = stmt.execute(config);
    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Statement<Integer> stmt =
        NativeSql.delete.from(e).where(c -> c.ge(e.salary, new Salary("2000")));

    int count = stmt.execute(config);
    assertEquals(6, count);
  }
}
