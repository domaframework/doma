package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.dialect.Dialect;

@ExtendWith(IntegrationTestEnvironment.class)
public class EntityqlInsertTest {

  private final Entityql entityql;
  private final Dialect dialect;

  public EntityqlInsertTest(Config config) {
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

    Result<Department> result = entityql.insert(d, department).execute();
    assertEquals(department, result.getEntity());

    Department department2 =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
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
    entityql.insert(b, book).execute();

    Book book2 = entityql.from(b).where(c -> c.eq(b.id, id)).fetchOne();
    assertEquals(id, book2.id);
    assertEquals("BOOK TITLE", book2.title);
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  public void onDuplicateKeyUpdate_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = entityql.insert(d, department).onDuplicateKeyUpdate().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  public void onDuplicateKeyUpdate_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = entityql.insert(d, department).onDuplicateKeyUpdate().execute();
    if (dialect.getName().equals("mysql") || dialect.getName().equals("mariadb")) {
      assertEquals(2, result1.getCount());
    } else {
      assertEquals(1, result1.getCount());
    }
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // updated
    assertEquals(60, resultDepartment.getDepartmentNo());
    assertEquals("DEVELOPMENT", resultDepartment.getDepartmentName());
    assertEquals("KYOTO", resultDepartment.getLocation());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
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
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  public void onDuplicateKeyIgnore_nonDuplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(5);
    department.setDepartmentNo(50);
    department.setDepartmentName("PLANNING");
    department.setLocation("TOKYO");
    Result<Department> result1 = entityql.insert(d, department).onDuplicateKeyIgnore().execute();
    assertEquals(1, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // inserted
    assertEquals(50, resultDepartment.getDepartmentNo());
    assertEquals("PLANNING", resultDepartment.getDepartmentName());
    assertEquals("TOKYO", resultDepartment.getLocation());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
  public void onDuplicateKeyIgnore_duplicated() {
    Department_ d = new Department_();

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(60);
    department.setDepartmentName("DEVELOPMENT");
    department.setLocation("KYOTO");
    Result<Department> result1 = entityql.insert(d, department).onDuplicateKeyIgnore().execute();
    assertEquals(0, result1.getCount());
    assertEquals(department, result1.getEntity());
    Department resultDepartment =
        entityql.from(d).where(c -> c.eq(d.departmentId, department.getDepartmentId())).fetchOne();
    // ignored
    assertEquals(10, resultDepartment.getDepartmentNo());
    assertEquals("ACCOUNTING", resultDepartment.getDepartmentName());
    assertEquals("NEW YORK", resultDepartment.getLocation());
  }

  @Test
  @Run(onlyIf = {Dbms.MYSQL, Dbms.POSTGRESQL}) // TODO: Implement it to work in other dialects
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
}
