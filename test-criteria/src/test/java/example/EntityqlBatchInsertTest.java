package example;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlBatchInsertTest {

  private final Entityql entityql;

  public EntityqlBatchInsertTest(Config config) {
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

    Department department2 = new Department();
    department2.setDepartmentId(100);
    department2.setDepartmentNo(100);
    department2.setDepartmentName("ccc");
    department2.setLocation("ddd");

    List<Department> departments = Arrays.asList(department, department2);

    Statement<List<Department>> insert = entityql.insert(d, departments);
    List<Department> results = insert.execute();
    assertEquals(departments, results);

    List<Integer> ids = departments.stream().map(it -> it.getDepartmentId()).collect(toList());

    Listable<Department> select =
        entityql.from(d).where(c -> c.in(d.departmentId, ids)).orderBy(c -> c.asc(d.departmentId));
    List<Department> departments2 = select.fetch();
    assertEquals(2, departments2.size());
  }

  @Test
  void empty() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> update = entityql.insert(e, Collections.emptyList());
    List<Employee> results = update.execute();
    assertTrue(results.isEmpty());
  }
}
