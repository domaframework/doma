package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class QueryDslEntityqlInsertTest {

  private final QueryDsl dsl;
  private final Dialect dialect;

  public QueryDslEntityqlInsertTest(Config config) {
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
}
