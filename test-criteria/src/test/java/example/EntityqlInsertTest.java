package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlInsertTest {

  private final Config config;

  public EntityqlInsertTest(Config config) {
    this.config = config;
  }

  @Test
  void test() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    assertEquals(department, Entityql.insert.into(d, department).execute(config));

    Statement<List<Department>> select =
        Entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId()));

    Department department2 = select.execute(config).get(0);
    assertEquals("aaa", department2.getDepartmentName());
  }
}
