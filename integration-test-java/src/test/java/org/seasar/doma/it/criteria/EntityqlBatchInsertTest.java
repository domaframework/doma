package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = Dbms.SQLITE)
public class EntityqlBatchInsertTest {
  private final Entityql entityql;
  private final Dialect dialect;

  public EntityqlBatchInsertTest(Config config) {
    this.entityql = new Entityql(config);
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

    BatchResult<Department> result = entityql.insert(d, departments).execute();
    assertEquals(departments, result.getEntities());

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
  void empty() {
    Employee_ e = new Employee_();

    BatchResult<Employee> result = entityql.insert(e, Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }

  @Test
  public void batch_onDuplicateKeyUpdate() {
    Department_ d = new Department_();

    Department department1 = new Department();
    department1.setDepartmentId(5);
    department1.setDepartmentNo(50);
    department1.setDepartmentName("PLANNING");
    department1.setLocation("TOKYO");
    Department department2 = new Department();
    department2.setDepartmentId(1);
    department2.setDepartmentNo(60);
    department2.setDepartmentName("DEVELOPMENT");
    department2.setLocation("KYOTO");

    List<Department> list = Arrays.asList(department1, department2);

    BatchResult<Department> result = entityql.insert(d, list).onDuplicateKeyUpdate().execute();
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertArrayEquals(new int[] {1, 2}, result.getCounts());
    } else {
      assertArrayEquals(new int[] {1, 1}, result.getCounts());
    }
    assertEquals(list, result.component1());

    Department resultDepartment1 =
        entityql.from(d).where(c -> c.eq(d.departmentId, department1.getDepartmentId())).fetchOne();
    Department resultDepartment2 =
        entityql.from(d).where(c -> c.eq(d.departmentId, department2.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment1.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment1.getDepartmentName());
    assertEquals("TOKYO", resultDepartment1.getLocation());
    // updated
    assertEquals(60, resultDepartment2.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment2.getDepartmentName());
    assertEquals("KYOTO", resultDepartment2.getLocation());
  }

  @Test
  public void batch_onDuplicateKeyIgnore() {
    Department_ d = new Department_();

    Department department1 = new Department();
    department1.setDepartmentId(5);
    department1.setDepartmentNo(50);
    department1.setDepartmentName("PLANNING");
    department1.setLocation("TOKYO");
    Department department2 = new Department();
    department2.setDepartmentId(1);
    department2.setDepartmentNo(60);
    department2.setDepartmentName("DEVELOPMENT");
    department2.setLocation("KYOTO");

    List<Department> list = Arrays.asList(department1, department2);

    BatchResult<Department> result = entityql.insert(d, list).onDuplicateKeyIgnore().execute();
    assertArrayEquals(new int[] {1, 0}, result.getCounts());
    assertEquals(list, result.component1());

    Department resultDepartment1 =
        entityql.from(d).where(c -> c.eq(d.departmentId, department1.getDepartmentId())).fetchOne();
    Department resultDepartment2 =
        entityql.from(d).where(c -> c.eq(d.departmentId, department2.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment1.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment1.getDepartmentName());
    assertEquals("TOKYO", resultDepartment1.getLocation());
    // ignored
    assertEquals(10, resultDepartment2.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment2.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment2.getLocation());
  }

  @Test
  public void onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result =
        entityql
            .insert(i, List.of(entity1, entity2))
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
      assertEquals(2, result.getEntities().get(1).getId());
    }

    var entities = entityql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  public void onDuplicateKeyUpdate_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result =
        entityql
            .insert(i, List.of(entity1, entity2))
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
      assertEquals(1, result.getEntities().get(1).getId());
    }

    var entities = entityql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entities.get(0).getId());
    }
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("B", entities.get(0).getValue());
  }

  @Test
  public void onDuplicateKeyIgnore_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result = entityql.insert(i, List.of(entity1, entity2)).onDuplicateKeyIgnore().execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
      assertEquals(2, result.getEntities().get(1).getId());
    }

    var entities = entityql.from(i).orderBy(c -> c.asc(i.id)).fetch();
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

    var result = entityql.insert(i, List.of(entity1, entity2)).onDuplicateKeyIgnore().execute();
    assertEquals(2, result.getEntities().size());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, result.getEntities().get(0).getId());
    }
    assertNull(result.getEntities().get(1).getId());

    var entities = entityql.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
  }
}
