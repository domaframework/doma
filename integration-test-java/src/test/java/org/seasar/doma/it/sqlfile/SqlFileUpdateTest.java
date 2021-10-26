package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.DeptDaoImpl;
import org.seasar.doma.it.dao.StaffDao;
import org.seasar.doma.it.dao.StaffDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.it.embeddable.StaffInfo;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.Staff;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileUpdateTest {

  @Test
  public void test(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(1));
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(1);
    int result = dao.updateBySqlFile(department);
    assertEquals(1, result);

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testPopulates(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(1));
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(1);
    int result = dao.updateBySqlFileWithPopulate(department);
    assertEquals(1, result);

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<Dept>(1), 1, "hoge", null, 1);
    Result<Dept> result = dao.updateBySqlFile(dept);
    assertEquals(1, result.getCount());
    dept = result.getEntity();
    assertEquals("hoge_preU_postU", dept.getDepartmentName());

    dept = dao.selectById(1);
    assertEquals(Integer.valueOf(1), dept.getDepartmentId().getValue());
    assertEquals("hoge_preU", dept.getDepartmentName());
    assertEquals(Integer.valueOf(2), dept.getVersion());
  }

  @Test
  public void testOptimisticLockException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department1 = dao.selectById(1);
    department1.setDepartmentName("hoge");
    Department department2 = dao.selectById(1);
    department2.setDepartmentName("foo");
    dao.updateBySqlFile(department1);
    try {
      dao.updateBySqlFile(department2);
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  @Test
  public void testSuppressOptimisticLockException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department1 = dao.selectById(1);
    department1.setDepartmentName("hoge");
    Department department2 = dao.selectById(1);
    department2.setDepartmentName("foo");
    dao.updateBySqlFile(department1);
    int rows = dao.updateBySqlFile_ignoreVersion(department2);
    assertEquals(0, rows);
  }

  @Test
  public void test_nonEntity(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(1));
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(1);
    int result = dao.updateBySqlFile_nonEntity(new Identity<Department>(1), 1, "hoge", null, 1);
    assertEquals(1, result);

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testEmbeddable(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = dao.selectById(1);
    staff.employeeName = "hoge";
    staff.staffInfo = new StaffInfo(13, staff.staffInfo.hiredate, new Salary("5000"));
    int result = dao.updateBySqlFile(staff);
    assertEquals(1, result);
    assertEquals(2, staff.version.intValue());
    staff = dao.selectById(1);
    assertEquals(5000L, staff.staffInfo.salary.getValue().longValue());
  }
}
