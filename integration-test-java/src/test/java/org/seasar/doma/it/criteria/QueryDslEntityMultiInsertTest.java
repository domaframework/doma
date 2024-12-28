package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = Dbms.SQLITE)
public class QueryDslEntityMultiInsertTest {
  private final QueryDsl dsl;
  private final Dialect dialect;

  public QueryDslEntityMultiInsertTest(Config config) {
    this.dsl = new QueryDsl(config);
    this.dialect = config.getDialect();
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

    MultiResult<Department> result = dsl.insert(d).multi(departments).execute();
    assertEquals(departments, result.getEntities());
    assertEquals(2, result.getCount());

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    List<Department> departments2 =
        dsl.from(d)
            .where(c -> c.in(d.departmentId, ids))
            .orderBy(c -> c.asc(d.departmentId))
            .fetch();
    assertEquals(2, departments2.size());
  }

  @Test
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

    dsl.insert(d).multi(Arrays.asList(department, department2)).execute();

    department.setDepartmentName("aaa_updated");
    department.setLocation("bbb_updated");

    List<Department> departments = Arrays.asList(department, department3);
    MultiResult<Department> result =
        dsl.insert(d).multi(departments).onDuplicateKeyIgnore().execute();
    assertEquals(departments, result.getEntities());
    assertEquals(1, result.getCount());

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    Map<Integer, Department> departments2 =
        dsl
            .from(d)
            .where(c -> c.in(d.departmentId, ids))
            .orderBy(c -> c.asc(d.departmentId))
            .stream()
            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
    assertEquals(2, departments2.size());
    assertEquals("aaa", departments2.get(99).getDepartmentName());
  }

  @Test
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

    dsl.insert(d).multi(Arrays.asList(department, department2)).execute();

    department.setDepartmentName("aaa_updated");
    department.setLocation("bbb_updated");

    List<Department> departments = Arrays.asList(department, department3);
    MultiResult<Department> result =
        dsl.insert(d).multi(departments).onDuplicateKeyUpdate().execute();
    assertEquals(departments, result.getEntities());
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(3, result.getCount());
    } else {
      assertEquals(2, result.getCount());
    }

    List<Integer> ids = departments.stream().map(Department::getDepartmentId).collect(toList());

    Map<Integer, Department> departments2 =
        dsl
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
  void empty() {
    Employee_ e = new Employee_();

    MultiResult<Employee> result = dsl.insert(e).multi(Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result =
        dsl.insert(i)
            .multi(List.of(entity1, entity2))
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .execute();
    assertEquals(2, result.getEntities().size());
    assertEquals(1, result.getEntities().get(0).getId());
    assertEquals(2, result.getEntities().get(1).getId());

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void onDuplicateKeyUpdate_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i).multi(List.of(entity1)).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    var result2 =
        dsl.insert(i).multi(List.of(entity2)).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();

    assertEquals(1, result1.getEntities().size());
    assertEquals(1, result1.getEntities().get(0).getId());
    assertEquals(1, result2.getEntities().size());
    assertEquals(1, result2.getEntities().get(0).getId());

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("B", entities.get(0).getValue());
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void onDuplicateKeyIgnore_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result = dsl.insert(i).multi(List.of(entity1, entity2)).onDuplicateKeyIgnore().execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
      assertEquals(2, result.getEntities().get(1).getId());
    }

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.POSTGRESQL})
  public void onDuplicateKeyIgnore_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result = dsl.insert(i).multi(List.of(entity1, entity2)).onDuplicateKeyIgnore().execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
    }
    assertNull(result.getEntities().get(1).getId());

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
  }
}
