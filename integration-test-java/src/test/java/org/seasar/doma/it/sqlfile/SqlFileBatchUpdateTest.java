package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
import org.seasar.doma.jdbc.BatchOptimisticLockException;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileBatchUpdateTest {

  @Test
  public void test(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(1));
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(1);
    Department department2 = new Department();
    department2.setDepartmentId(new Identity<Department>(2));
    department2.setDepartmentNo(2);
    department2.setDepartmentName("foo");
    department2.setVersion(1);
    int[] result = dao.updateBySqlFile(Arrays.asList(department, department2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals(Integer.valueOf(2), department.getVersion());
    department = dao.selectById(2);
    assertEquals(Integer.valueOf(2), department.getDepartmentId().getValue());
    assertEquals("foo", department.getDepartmentName());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<Dept>(1), 1, "hoge", null, 1);
    Dept dept2 = new Dept(new Identity<Dept>(2), 2, "foo", null, 1);
    BatchResult<Dept> result = dao.updateBySqlFile(Arrays.asList(dept, dept2));
    int[] counts = result.getCounts();
    assertEquals(2, counts.length);
    assertEquals(1, counts[0]);
    assertEquals(1, counts[1]);
    dept = result.getEntities().get(0);
    assertEquals("hoge_preU_postU", dept.getDepartmentName());
    dept2 = result.getEntities().get(1);
    assertEquals("foo_preU_postU", dept2.getDepartmentName());

    dept = dao.selectById(1);
    assertEquals(Integer.valueOf(1), dept.getDepartmentId().getValue());
    assertEquals("hoge_preU", dept.getDepartmentName());
    assertEquals(Integer.valueOf(2), dept.getVersion());
    dept2 = dao.selectById(2);
    assertEquals(Integer.valueOf(2), dept2.getDepartmentId().getValue());
    assertEquals("foo_preU", dept2.getDepartmentName());
    assertEquals(Integer.valueOf(2), dept2.getVersion());
  }

  @Test
  public void testOptimisticLockException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department1 = dao.selectById(1);
    department1.setDepartmentName("hoge");
    Department department2 = dao.selectById(2);
    department2.setDepartmentName("foo");
    Department department3 = dao.selectById(1);
    department3.setDepartmentName("bar");
    dao.updateBySqlFile(department1);
    try {
      dao.updateBySqlFile(Arrays.asList(department2, department3));
      fail();
    } catch (BatchOptimisticLockException expected) {
    }
  }

  @Test
  public void testSuppressOptimisticLockException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department1 = dao.selectById(1);
    department1.setDepartmentName("hoge");
    Department department2 = dao.selectById(2);
    department2.setDepartmentName("foo");
    Department department3 = dao.selectById(1);
    department3.setDepartmentName("bar");
    dao.update(department1);
    dao.updateBySqlFile_suppressOptimisticLockException(Arrays.asList(department2, department3));
  }
}
