package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.BranchDao;
import org.seasar.doma.it.dao.BranchDao.Branch;
import org.seasar.doma.it.dao.BranchDao.BranchDetail;
import org.seasar.doma.it.dao.BranchDao.Location;
import org.seasar.doma.it.dao.BranchDaoImpl;
import org.seasar.doma.it.dao.BusinessmanDao;
import org.seasar.doma.it.dao.BusinessmanDaoImpl;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.CompKeyDepartmentDaoImpl;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.DeptDaoImpl;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDaoImpl;
import org.seasar.doma.it.dao.SalesmanDao;
import org.seasar.doma.it.dao.SalesmanDaoImpl;
import org.seasar.doma.it.dao.StaffDao;
import org.seasar.doma.it.dao.StaffDaoImpl;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.dao.WorkerDaoImpl;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.it.embeddable.StaffInfo;
import org.seasar.doma.it.entity.Businessman;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.Salesman;
import org.seasar.doma.it.entity.Staff;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class AutoUpdateTest {

  @Test
  public void test(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectById(1);
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    int result = dao.update(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(2), department.getVersion());

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(1), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("NEW YORK", department.getLocation().getValue());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = dao.selectById(1);
    dept = new Dept(dept.getDepartmentId(), 1, "hoge", dept.getLocation(), dept.getVersion());
    Result<Dept> result = dao.update(dept);
    assertEquals(1, result.getCount());
    dept = result.getEntity();
    assertEquals(Integer.valueOf(2), dept.getVersion());
    assertEquals("hoge_preU_postU", dept.getDepartmentName());

    dept = dao.selectById(1);
    assertEquals(Integer.valueOf(1), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(1), dept.getDepartmentNo());
    assertEquals("hoge_preU", dept.getDepartmentName());
    assertEquals("NEW YORK", dept.getLocation().getValue());
    assertEquals(Integer.valueOf(2), dept.getVersion());
  }

  @Test
  public void testIgnoreVersion(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectById(1);
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(100);
    int result = dao.update_ignoreVersion(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(100), department.getVersion());

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(1), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("NEW YORK", department.getLocation().getValue());
    assertEquals(Integer.valueOf(100), department.getVersion());
  }

  @Test
  public void testExcludeNull(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectById(1);
    department.setDepartmentNo(1);
    department.setDepartmentName(null);
    int result = dao.update_excludeNull(department);
    assertEquals(1, result);

    department = dao.selectById(1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(1), department.getDepartmentNo());
    assertEquals("ACCOUNTING", department.getDepartmentName());
    assertEquals("NEW YORK", department.getLocation().getValue());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testCompositeKey(Config config) throws Exception {
    CompKeyDepartmentDao dao = new CompKeyDepartmentDaoImpl(config);
    CompKeyDepartment department = dao.selectById(1, 1);
    department.setDepartmentNo(1);
    department.setDepartmentName("hoge");
    department.setVersion(1);
    int result = dao.update(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(2), department.getVersion());

    department = dao.selectById(1, 1);
    assertEquals(Integer.valueOf(1), department.getDepartmentId1());
    assertEquals(Integer.valueOf(1), department.getDepartmentId2());
    assertEquals(Integer.valueOf(1), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("NEW YORK", department.getLocation());
    assertEquals(Integer.valueOf(2), department.getVersion());
  }

  @Test
  public void testOptimisticLockException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department1 = dao.selectById(1);
    department1.setDepartmentName("hoge");
    Department department2 = dao.selectById(1);
    department2.setDepartmentName("foo");
    dao.update(department1);
    try {
      dao.update(department2);
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
    dao.update(department1);
    int rows = dao.update_suppressOptimisticLockException(department2);
    assertEquals(0, rows);
  }

  @Test
  public void testNoId(Config config) throws Exception {
    NoIdDao dao = new NoIdDaoImpl(config);
    NoId entity = new NoId();
    entity.setValue1(1);
    entity.setValue2(2);
    try {
      dao.update(entity);
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2022, expected.getMessageResource());
    }
  }

  @Test
  public void testSqlExecutionSkip(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectById(1);
    int result = dao.update(department);
    assertEquals(0, result);
  }

  @Test
  public void testOptional(Config config) throws Exception {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker worker = dao.selectById(Optional.of(1));
    worker.employeeName = Optional.of("hoge");
    int result = dao.update(worker);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(2), worker.version.get());

    worker = dao.selectById(Optional.of(1));
    assertEquals(Integer.valueOf(7369), worker.employeeNo.get());
    assertEquals(Integer.valueOf(2), worker.version.get());
    assertEquals("hoge", worker.employeeName.get());
    assertEquals(0, worker.salary.get().getValue().compareTo(new BigDecimal("800")));
    assertEquals(java.sql.Date.valueOf("1980-12-17"), worker.hiredate.get());
    assertEquals(Integer.valueOf(13), worker.managerId.get());
    assertEquals(Integer.valueOf(2), worker.departmentId.get().getValue());
    assertEquals(Integer.valueOf(1), worker.addressId.get());
  }

  @Test
  public void testOptionalInt(Config config) throws Exception {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman worker = dao.selectById(OptionalInt.of(1));
    worker.employeeName = Optional.of("hoge");
    int result = dao.update(worker);
    assertEquals(1, result);
    assertEquals(2, worker.version.getAsInt());

    worker = dao.selectById(OptionalInt.of(1));
    assertEquals(7369, worker.employeeNo.getAsInt());
    assertEquals(2, worker.version.getAsInt());
    assertEquals("hoge", worker.employeeName.get());
    assertEquals(800L, worker.salary.getAsLong());
    assertEquals(java.sql.Date.valueOf("1980-12-17"), worker.hiredate.get());
    assertEquals(13, worker.managerId.getAsInt());
    assertEquals(2, worker.departmentId.getAsInt());
    assertEquals(1, worker.addressId.getAsInt());
  }

  @Test
  public void testUpdate(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = dao.selectById(1);
    staff.employeeName = "hoge";
    staff.staffInfo =
        new StaffInfo(staff.staffInfo.managerId, staff.staffInfo.hiredate, new Salary("5000"));
    int result = dao.update(staff);
    assertEquals(1, result);
    assertEquals(2, staff.version.intValue());

    staff = dao.selectById(1);
    assertEquals(7369, staff.employeeNo.intValue());
    assertEquals(2, staff.version.intValue());
    assertEquals("hoge", staff.employeeName);
    assertEquals(5000L, staff.staffInfo.salary.getValue().longValue());
    assertEquals(java.sql.Date.valueOf("1980-12-17"), staff.staffInfo.hiredate);
    assertEquals(13, staff.staffInfo.managerId);
    assertEquals(2, staff.departmentId.intValue());
    assertEquals(1, staff.addressId.intValue());
  }

  @Test
  public void testNestedEntity(Config config) throws Exception {
    BranchDao dao = new BranchDaoImpl(config);
    {
      Branch branch = dao.selectById(1);
      assertNotNull(branch);
      assertEquals(Integer.valueOf(1), branch.version);
      BranchDetail branchDetail = branch.branchDetail;
      assertNotNull(branchDetail);
      branchDetail.location = new Location("foo");
      dao.update(branch);
    }
    {
      Branch branch = dao.selectById(1);
      assertNotNull(branch);
      assertEquals(Integer.valueOf(2), branch.version);
      BranchDetail branchDetail = branch.branchDetail;
      assertNotNull(branchDetail);
      Location location = branchDetail.location;
      assertNotNull(location);
      assertEquals("foo", location.getValue());
    }
  }

  @Test
  public void testTenantId(Config config) throws Exception {
    SalesmanDao dao = new SalesmanDaoImpl(config);
    Salesman salesman = dao.selectById(1);
    Integer tenantId = salesman.departmentId;
    salesman.departmentId = -1;
    try {
      dao.update(salesman);
      fail();
    } catch (OptimisticLockException expected) {
    }
    salesman.departmentId = tenantId;
    dao.update(salesman);
  }
}
