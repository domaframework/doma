package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;
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

    Statement<Department> insert = entityql.insert(d, department);
    Department result = insert.execute();

    assertEquals(department, result);

    Listable<Department> select =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId()));
    Department department2 = select.getSingleResult().orElseThrow(AssertionError::new);
    assertEquals("aaa", department2.getDepartmentName());
  }
}
