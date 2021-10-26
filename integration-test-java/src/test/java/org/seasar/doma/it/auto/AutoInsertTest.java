package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Date;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.BranchDao;
import org.seasar.doma.it.dao.BranchDao.Branch;
import org.seasar.doma.it.dao.BranchDao.BranchDetail;
import org.seasar.doma.it.dao.BranchDaoImpl;
import org.seasar.doma.it.dao.BusinessmanDao;
import org.seasar.doma.it.dao.BusinessmanDaoImpl;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.CompKeyDepartmentDaoImpl;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.DeptDaoImpl;
import org.seasar.doma.it.dao.IdentityStrategyDao;
import org.seasar.doma.it.dao.IdentityStrategyDaoImpl;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDaoImpl;
import org.seasar.doma.it.dao.PrimitiveIdentityStrategyDao;
import org.seasar.doma.it.dao.PrimitiveIdentityStrategyDaoImpl;
import org.seasar.doma.it.dao.SequenceStrategyDao;
import org.seasar.doma.it.dao.SequenceStrategyDaoImpl;
import org.seasar.doma.it.dao.StaffDao;
import org.seasar.doma.it.dao.StaffDaoImpl;
import org.seasar.doma.it.dao.TableStrategyDao;
import org.seasar.doma.it.dao.TableStrategyDaoImpl;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.dao.WorkerDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.it.embeddable.StaffInfo;
import org.seasar.doma.it.entity.Businessman;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.PrimitiveIdentityStrategy;
import org.seasar.doma.it.entity.SequenceStrategy;
import org.seasar.doma.it.entity.Staff;
import org.seasar.doma.it.entity.TableStrategy;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class AutoInsertTest {

  @Test
  public void test(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    department.setLocation(new Location<Department>("foo"));
    int result = dao.insert(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(Integer.valueOf(99));
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("foo", department.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department.getVersion());
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<Dept>(99), 99, "hoge", new Location<Dept>("foo"), null);
    Result<Dept> result = dao.insert(dept);
    assertEquals(1, result.getCount());
    dept = result.getEntity();
    assertEquals(Integer.valueOf(1), dept.getVersion());
    assertEquals("hoge_preI_postI", dept.getDepartmentName());

    dept = dao.selectById(Integer.valueOf(99));
    assertEquals(Integer.valueOf(99), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), dept.getDepartmentNo());
    assertEquals("hoge_preI", dept.getDepartmentName());
    assertEquals("foo", dept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), dept.getVersion());
  }

  @Test
  public void test_UniqueConstraintException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insert(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());
    try {
      dao.insert(department);
      fail();
    } catch (UniqueConstraintException e) {
    }
  }

  @Test
  public void testExcludeNull(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insert_excludeNull(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(Integer.valueOf(99));
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("TOKYO", department.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department.getVersion());
  }

  @Test
  public void testCompositeKey(Config config) throws Exception {
    CompKeyDepartmentDao dao = new CompKeyDepartmentDaoImpl(config);
    CompKeyDepartment department = new CompKeyDepartment();
    department.setDepartmentId1(99);
    department.setDepartmentId2(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insert(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(99, 99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId1());
    assertEquals(Integer.valueOf(99), department.getDepartmentId2());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertNull(department.getLocation());
    assertEquals(Integer.valueOf(1), department.getVersion());
  }

  @Test
  public void testIdNotAssigned(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    try {
      dao.insert(department);
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2020, expected.getMessageResource());
    }
  }

  @Test
  public void testId_Identity(Config config) throws Exception {
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      IdentityStrategy entity = new IdentityStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
  }

  @Test
  public void testId_PrimitiveIdentity(Config config) throws Exception {
    PrimitiveIdentityStrategyDao dao = new PrimitiveIdentityStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      PrimitiveIdentityStrategy entity = new PrimitiveIdentityStrategy();
      dao.insert(entity);
      assertTrue(entity.getId() > 0);
    }
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.SQLSERVER, Dbms.SQLITE})
  public void testId_sequence(Config config) throws Exception {
    SequenceStrategyDao dao = new SequenceStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      SequenceStrategy entity = new SequenceStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
  }

  // it seems that sqlite doesn't support requiresNew transaction
  // so ignore this test case
  @Test
  @Run(unless = {Dbms.SQLITE})
  public void testId_table(Config config) throws Exception {
    TableStrategyDao dao = new TableStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      TableStrategy entity = new TableStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
  }

  @Test
  public void testNoId(Config config) throws Exception {
    NoIdDao dao = new NoIdDaoImpl(config);
    NoId entity = new NoId();
    entity.setValue1(1);
    entity.setValue2(2);
    int result = dao.insert(entity);
    assertEquals(1, result);
  }

  @Test
  public void testOptional(Config config) throws Exception {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker worker = new Worker();
    worker.employeeId = Optional.of(9999);
    worker.employeeNo = Optional.of(9999);
    int result = dao.insert(worker);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), worker.version.get());

    worker = dao.selectById(Optional.of(9999));
    assertEquals(Integer.valueOf(9999), worker.employeeNo.get());
    assertEquals(Integer.valueOf(1), worker.version.get());
    assertFalse(worker.employeeName.isPresent());
    assertFalse(worker.salary.isPresent());
    assertFalse(worker.hiredate.isPresent());
    assertFalse(worker.managerId.isPresent());
    assertFalse(worker.departmentId.isPresent());
    assertFalse(worker.addressId.isPresent());
  }

  @Test
  public void testOptionalInt(Config config) throws Exception {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman worker = new Businessman();
    worker.employeeId = OptionalInt.of(9999);
    worker.employeeNo = OptionalInt.of(9999);
    int result = dao.insert(worker);
    assertEquals(1, result);
    assertEquals(1, worker.version.getAsInt());

    worker = dao.selectById(OptionalInt.of(9999));
    assertEquals(9999, worker.employeeNo.getAsInt());
    assertEquals(1, worker.version.getAsInt());
    assertFalse(worker.employeeName.isPresent());
    assertFalse(worker.salary.isPresent());
    assertFalse(worker.hiredate.isPresent());
    assertFalse(worker.managerId.isPresent());
    assertFalse(worker.departmentId.isPresent());
    assertFalse(worker.addressId.isPresent());
  }

  @Test
  public void testEmbeddable(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 9999;
    staff.employeeNo = 9999;
    staff.staffInfo = new StaffInfo(13, Date.valueOf("2016-05-27"), new Salary("1234"));
    int result = dao.insert(staff);
    assertEquals(1, result);
    assertEquals(1, staff.version.intValue());

    staff = dao.selectById(9999);
    StaffInfo staffInfo = staff.staffInfo;
    assertNotNull(staffInfo);
    assertEquals(Date.valueOf("2016-05-27"), staffInfo.hiredate);
    assertEquals(1234L, staffInfo.salary.getValue().longValue());
  }

  @Test
  public void testEmbeddable_null(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 9999;
    staff.employeeNo = 9999;
    int result = dao.insert(staff);
    assertEquals(1, result);
    assertEquals(1, staff.version.intValue());

    staff = dao.selectById(9999);
    StaffInfo staffInfo = staff.staffInfo;
    assertNotNull(staffInfo);
    assertNull(staffInfo.hiredate);
    assertNull(staffInfo.salary);
  }

  @Test
  public void testNestedEntity(Config config) throws Exception {
    BranchDao dao = new BranchDaoImpl(config);
    {
      Branch branch = new Branch();
      branch.branchId = 99;
      branch.branchDetail =
          new BranchDetail(99, "hoge", new org.seasar.doma.it.dao.BranchDao.Location("foo"));
      dao.insert(branch);
    }
    {
      Branch branch = dao.selectById(99);
      assertNotNull(branch);
      assertEquals(Integer.valueOf(99), branch.branchId);
      assertEquals(Integer.valueOf(1), branch.version);
      BranchDetail branchDetail = branch.branchDetail;
      assertNotNull(branchDetail);
      assertEquals(Integer.valueOf(99), branchDetail.branchNo);
      assertEquals("hoge", branchDetail.branchName);
      org.seasar.doma.it.dao.BranchDao.Location location = branchDetail.location;
      assertNotNull(location);
      assertEquals("foo", location.getValue());
    }
  }
}
