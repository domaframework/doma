package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;

@ExtendWith(IntegrationTestEnvironment.class)
public class ImmutableTest {

  private final Entityql entityql;

  public ImmutableTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void fetch() {
    Emp_ e = new Emp_();

    List<Emp> list = entityql.from(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void associateWith() {
    Emp_ e = new Emp_();
    Emp_ m = new Emp_();
    Dept_ d = new Dept_();

    List<Emp> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .leftJoin(m, on -> on.eq(e.managerId, m.employeeId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associateWith(e, d, Emp::withDept)
            .associateWith(e, m, Emp::withManager)
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertTrue(list.stream().allMatch(it -> Objects.nonNull(it.getManager())));
  }
}
