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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslEntityInsertTest {

  private final QueryDsl dsl;
  private final Dialect dialect;

  public QueryDslEntityInsertTest(Config config) {
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

    Result<Department> result = dsl.insert(d).single(department).execute();
    assertEquals(department, result.getEntity());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
  }

  @Run(onlyIf = Dbms.POSTGRESQL)
  @Test
  public void uuid() {
    Book_ b = new Book_();
    UUID id = UUID.randomUUID();

    Book book = new Book();
    book.id = id;
    book.title = "BOOK TITLE";
    dsl.insert(b).single(book).execute();

    Book book2 = dsl.from(b).where(c -> c.eq(b.id, id)).fetchOne();
    assertEquals(id, book2.id);
    assertEquals("BOOK TITLE", book2.title);
  }

  @Test
  public void onDuplicateKeyUpdate_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyUpdate().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyUpdate_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyUpdate().execute();
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, result1.getCount());
    } else {
      assertEquals(1, result1.getCount());
    }
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyIgnore_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyIgnore().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  public void onDuplicateKeyIgnore_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = dsl.insert(d).single(department).onDuplicateKeyIgnore().execute();
    assertEquals(0, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // ignored
    assertEquals(10, resultDepartment.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment.getLocation());
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

    var result1 =
        dsl.insert(i).single(entity1).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 =
        dsl.insert(i).single(entity2).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity2, result2.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(2, entity2.getId());
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
  public void onDuplicateKeyUpdate_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i).single(entity1).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 =
        dsl.insert(i).single(entity2).onDuplicateKeyUpdate().keys(i.uniqueValue).execute();
    assertEquals(entity2, result2.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity2.getId());
    }

    var entities = dsl.from(i).orderBy(c -> c.asc(i.id)).fetch();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
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

    var result1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().execute();
    assertEquals(entity2, result2.getEntity());
    assertEquals(2, entity2.getId());

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

    var result1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().execute();
    assertEquals(entity1, result1.getEntity());
    if (!dialect.getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
    }
    var result2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().execute();
    assertEquals(entity2, result2.getEntity());
    assertNull(entity2.getId());

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

    Result<Department> result = dsl.insert(d).single(department).returning().execute();
    assertNotEquals(department, result.getEntity());
    assertEquals(1, result.getCount());
    Department resultEntity = result.getEntity();
    assertEquals(department.getDepartmentId(), resultEntity.getDepartmentId());
    assertEquals(department.getDepartmentNo(), resultEntity.getDepartmentNo());
    assertEquals(department.getDepartmentName(), resultEntity.getDepartmentName());
    assertEquals(department.getLocation(), resultEntity.getLocation());
    assertEquals(1, resultEntity.getVersion());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
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

    Result<Department> result =
        dsl.insert(d).single(department).returning(d.departmentNo, d.departmentName).execute();
    assertNotEquals(department, result.getEntity());
    assertEquals(1, result.getCount());
    Department resultEntity = result.getEntity();
    assertNull(resultEntity.getDepartmentId());
    assertEquals(department.getDepartmentNo(), resultEntity.getDepartmentNo());
    assertEquals(department.getDepartmentName(), resultEntity.getDepartmentName());
    assertNull(resultEntity.getLocation());
    assertNull(resultEntity.getVersion());

    Department department2 =
        dsl.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    assertNotNull(department2);
    assertEquals("aaa", department2.getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");

    var result1 = dsl.insert(i).single(entity1).returning().execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(entity1, result1.getEntity());
    var resultEntity = result1.getEntity();
    assertNotNull(resultEntity.getId());
    assertEquals("1", resultEntity.getUniqueValue());
    assertEquals("A", resultEntity.getValue());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 =
        dsl.insert(d).single(department).onDuplicateKeyUpdate().returning().execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(department, result1.getEntity());

    Department resultDepartment = result1.getEntity();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 =
        dsl.insert(d).single(department).onDuplicateKeyUpdate().returning().execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(department, result1.getEntity());
    Department resultDepartment = result1.getEntity();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyIgnore_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 =
        dsl.insert(d).single(department).onDuplicateKeyIgnore().returning().execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(department, result1.getEntity());
    Department resultDepartment = result1.getEntity();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyIgnore_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 =
        dsl.insert(d).single(department).onDuplicateKeyIgnore().returning().execute();
    assertEquals(0, result1.getCount());
    assertNull(result1.getEntity());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_onDuplicateKeyUpdate_nonDuplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    var result1 =
        dsl.insert(i)
            .single(entity1)
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(entity1, result1.getEntity());
    var resultEntity1 = result1.getEntity();
    assertNotNull(resultEntity1.getId());
    assertEquals("1", resultEntity1.getUniqueValue());
    assertEquals("A", resultEntity1.getValue());

    var result2 =
        dsl.insert(i)
            .single(entity2)
            .onDuplicateKeyUpdate()
            .keys(i.uniqueValue)
            .returning()
            .execute();
    assertEquals(1, result2.getCount());
    assertNotEquals(entity2, result2.getEntity());
    var resultEntity2 = result2.getEntity();
    assertNotNull(resultEntity2.getId());
    assertEquals("2", resultEntity2.getUniqueValue());
    assertEquals("B", resultEntity2.getValue());
  }

  @Test
  @Run(onlyIf = {Dbms.POSTGRESQL, Dbms.SQLITE})
  public void returning_onDuplicateKeyIgnore_duplicated_identityTable() {
    IdentityTable_ i = new IdentityTable_();

    var entity1 = new IdentityTable();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityTable();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    var result1 = dsl.insert(i).single(entity1).onDuplicateKeyIgnore().returning().execute();
    assertEquals(1, result1.getCount());
    assertNotEquals(entity1, result1.getEntity());
    var resultEntity1 = result1.getEntity();
    assertNotNull(resultEntity1.getId());
    assertEquals("1", resultEntity1.getUniqueValue());
    assertEquals("A", resultEntity1.getValue());

    var result2 = dsl.insert(i).single(entity2).onDuplicateKeyIgnore().returning().execute();
    assertEquals(0, result2.getCount());
    assertNull(result2.getEntity());
  }
}
