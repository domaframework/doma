package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.DeptDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileBatchInsertTest {

  @Test
  public void test(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    Department department2 = new Department();
    department2.setDepartmentId(new Identity<>(98));
    department2.setDepartmentNo(98);
    department2.setDepartmentName("foo");
    int[] result = dao.insertBySqlFile(Arrays.asList(department, department2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    department = dao.selectById(99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    department = dao.selectById(98);
    assertEquals(Integer.valueOf(98), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(98), department.getDepartmentNo());
  }

  @Test
  public void testImmutable(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(99), 99, "hoge", null, null);
    Dept dept2 = new Dept(new Identity<>(98), 98, "foo", null, null);
    BatchResult<Dept> result = dao.insertBySqlFile(Arrays.asList(dept, dept2));
    int[] counts = result.getCounts();
    assertEquals(2, counts.length);
    assertEquals(1, counts[0]);
    assertEquals(1, counts[1]);
    dept = result.getEntities().get(0);
    assertEquals("hoge_preI(E)_postI(E)", dept.getDepartmentName());
    dept2 = result.getEntities().get(1);
    assertEquals("foo_preI(E)_postI(E)", dept2.getDepartmentName());

    dept = dao.selectById(99);
    assertEquals(Integer.valueOf(99), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), dept.getDepartmentNo());
    assertEquals("hoge_preI(E)", dept.getDepartmentName());
    dept2 = dao.selectById(98);
    assertEquals(Integer.valueOf(98), dept2.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(98), dept2.getDepartmentNo());
    assertEquals("foo_preI(E)", dept2.getDepartmentName());
  }
}
