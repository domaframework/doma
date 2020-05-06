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

  private final Entityql entityql;

  public EntityqlInsertTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    assertEquals(department, entityql.insert(d, department).execute());

    Statement<List<Department>> select =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId()));

    Department department2 = select.execute().get(0);
    assertEquals("aaa", department2.getDepartmentName());
  }
}
