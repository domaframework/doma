package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
  void testIgnore() {
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

    Department department3 = new Department();
    department3.setDepartmentId(101);
    department3.setDepartmentNo(101);
    department3.setDepartmentName("eee");
    department3.setLocation("fff");

    entityql.insertMulti(d, Arrays.asList(department, department2)).execute();

    department.setDepartmentName("aaa_updated");
    department.setLocation("bbb_updated");

    List<Department> departments = Arrays.asList(department, department3);
    MultiResult<Department> result =
        entityql.insertMulti(d, departments).onDuplicateKeyIgnore().execute();
    assertEquals(departments, result.getEntities());
    assertEquals(1, result.getCount());

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    Map<Integer, Department> departments2 =
        entityql
            .from(d)
            .where(c -> c.in(d.departmentId, ids))
            .orderBy(c -> c.asc(d.departmentId))
            .stream()
            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
    assertEquals(2, departments2.size());
    assertEquals("aaa", departments2.get(99).getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  void testUpdate() {
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

    Department department3 = new Department();
    department3.setDepartmentId(101);
    department3.setDepartmentNo(101);
    department3.setDepartmentName("eee");
    department3.setLocation("fff");

    entityql.insertMulti(d, Arrays.asList(department, department2)).execute();

    department.setDepartmentName("aaa_updated");
    department.setLocation("bbb_updated");

    List<Department> departments = Arrays.asList(department, department3);
    MultiResult<Department> result =
        entityql.insertMulti(d, departments).onDuplicateKeyUpdate().execute();
    assertEquals(departments, result.getEntities());
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(3, result.getCount());
    } else {
      assertEquals(2, result.getCount());
    }

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    Map<Integer, Department> departments2 =
        entityql
            .from(d)
            .where(c -> c.in(d.departmentId, ids))
            .orderBy(c -> c.asc(d.departmentId))
            .stream()
            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
    assertEquals(2, departments2.size());
    assertEquals("aaa_updated", departments2.get(99).getDepartmentName());
    assertEquals("eee", departments2.get(101).getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  void empty() {
    Employee_ e = new Employee_();

    MultiResult<Employee> result = entityql.insertMulti(e, Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }
}
