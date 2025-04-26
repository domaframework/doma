/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
  @Run(unless = {Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
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
  @Run(unless = {Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
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
  @Run(unless = {Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
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

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning() {
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

    MultiResult<Department> result = dsl.insert(d).multi(departments).returning().execute();
    assertNotEquals(departments, result.getEntities());
    assertEquals(2, result.getCount());

    var entity1 = result.getEntities().get(0);
    assertEquals(99, entity1.getDepartmentId());
    assertEquals(99, entity1.getDepartmentNo());
    assertEquals("aaa", entity1.getDepartmentName());
    assertEquals("bbb", entity1.getLocation());
    assertEquals(1, entity1.getVersion());

    var entity2 = result.getEntities().get(1);
    assertEquals(100, entity2.getDepartmentId());
    assertEquals(100, entity2.getDepartmentNo());
    assertEquals("ccc", entity2.getDepartmentName());
    assertEquals("ddd", entity2.getLocation());
    assertEquals(1, entity2.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_specificProperties() {
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

    MultiResult<Department> result =
        dsl.insert(d).multi(departments).returning(d.departmentNo, d.departmentName).execute();
    assertNotEquals(departments, result.getEntities());
    assertEquals(2, result.getCount());

    var entity1 = result.getEntities().get(0);
    assertNull(entity1.getDepartmentId());
    assertEquals(99, entity1.getDepartmentNo());
    assertEquals("aaa", entity1.getDepartmentName());
    assertNull(entity1.getLocation());
    assertNull(entity1.getVersion());

    var entity2 = result.getEntities().get(1);
    assertNull(entity2.getDepartmentId());
    assertEquals(100, entity2.getDepartmentNo());
    assertEquals("ccc", entity2.getDepartmentName());
    assertNull(entity2.getLocation());
    assertNull(entity2.getVersion());
  }

  @Test
  void returning_skip() {
    Department_ d = new Department_();

    MultiResult<Department> result =
        dsl.insert(d).multi(Collections.emptyList()).returning().execute();
    assertTrue(result.getEntities().isEmpty());
    assertEquals(0, result.getCount());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_ignore() {
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
        dsl.insert(d).multi(departments).onDuplicateKeyIgnore().returning().execute();
    assertNotEquals(departments, result.getEntities());
    assertEquals(1, result.getCount());

    var entity1 = result.getEntities().get(0);
    assertEquals(101, entity1.getDepartmentId());
    assertEquals(101, entity1.getDepartmentNo());
    assertEquals("eee", entity1.getDepartmentName());
    assertEquals("fff", entity1.getLocation());
    assertEquals(1, entity1.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_update() {
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
        dsl.insert(d).multi(departments).onDuplicateKeyUpdate().returning().execute();
    assertNotEquals(departments, result.getEntities());
    assertEquals(2, result.getCount());

    var entity1 = result.getEntities().get(0);
    assertEquals(99, entity1.getDepartmentId());
    assertEquals(99, entity1.getDepartmentNo());
    assertEquals("aaa_updated", entity1.getDepartmentName());
    assertEquals("bbb_updated", entity1.getLocation());
    assertEquals(1, entity1.getVersion());

    var entity2 = result.getEntities().get(1);
    assertEquals(101, entity2.getDepartmentId());
    assertEquals(101, entity2.getDepartmentNo());
    assertEquals("eee", entity2.getDepartmentName());
    assertEquals("fff", entity2.getLocation());
    assertEquals(1, entity2.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
  public void returning_onDuplicateKeyUpdate_nonDuplicated_identityTable() {
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
            .returning()
            .execute();
    assertEquals(2, result.getCount());
    assertEquals(2, result.getEntities().size());
    assertEquals(1, result.getEntities().get(0).getId());
    assertEquals(2, result.getEntities().get(1).getId());

    var entities = result.getEntities();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
  public void returning_onDuplicateKeyUpdate_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i)
            .multi(List.of(entity1))
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    var result2 =
        dsl.insert(i)
            .multi(List.of(entity2))
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .execute();

    assertEquals(1, result1.getCount());
    assertEquals(1, result1.getEntities().size());
    assertEquals(1, result2.getCount());
    assertEquals(1, result2.getEntities().size());

    var resultEntity1 = result1.getEntities().get(0);
    assertEquals(1, resultEntity1.getId());
    assertEquals("1", resultEntity1.getUniqueValue());
    assertEquals("A", resultEntity1.getValue());

    var resultEntity2 = result2.getEntities().get(0);
    assertEquals(1, resultEntity2.getId());
    assertEquals("1", resultEntity2.getUniqueValue());
    assertEquals("B", resultEntity2.getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
  public void returning_onDuplicateKeyIgnore_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i)
            .multi(List.of(entity1))
            .onDuplicateKeyIgnore()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    assertEquals(1, result1.getCount());
    assertEquals(1, result1.getEntities().size());

    var result2 =
        dsl.insert(i)
            .multi(List.of(entity2))
            .onDuplicateKeyIgnore()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    assertEquals(0, result2.getCount());
    assertEquals(0, result2.getEntities().size());

    var entity = result1.getEntities().get(0);
    assertEquals(1, entity.getId());
    assertEquals("1", entity.getUniqueValue());
    assertEquals("A", entity.getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE, Dbms.SQLITE, Dbms.SQLSERVER})
  public void returning_onDuplicateKeyIgnore_duplicated_atOnce_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result =
        dsl.insert(i)
            .multi(List.of(entity1, entity2))
            .onDuplicateKeyIgnore()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    assertEquals(1, result.getCount());
    assertEquals(1, result.getEntities().size());

    var entity = result.getEntities().get(0);
    assertEquals(1, entity.getId());
    assertEquals("1", entity.getUniqueValue());
    assertEquals("A", entity.getValue());
  }
}
