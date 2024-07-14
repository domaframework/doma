package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
public class EntityqlMultiInsertTest {
  private final Entityql entityql;
  private final Dialect dialect;

  public EntityqlMultiInsertTest(Config config) {
    this.entityql = new Entityql(config);
    this.dialect = config.getDialect();
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
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

    MultiResult<Department> result = entityql.insertMulti(d, departments).execute();
    assertEquals(departments, result.getEntities());
    assertEquals(2, result.getCount());

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    List<Department> departments2 =
        entityql
            .from(d)
            .where(c -> c.in(d.departmentId, ids))
            .orderBy(c -> c.asc(d.departmentId))
            .fetch();
    assertEquals(2, departments2.size());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  void empty() {
    Employee_ e = new Employee_();

    MultiResult<Employee> result = entityql.insertMulti(e, Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }
}
