package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class AutoMultiInsertTest {

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void test(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    department.setLocation(new Location<>("foo"));

    Department department2 = new Department();
    department2.setDepartmentId(new Identity<>(100));
    department2.setDepartmentNo(100);
    department2.setDepartmentName("bar");
    department2.setLocation(new Location<>("boo"));

    int result = dao.insertMultiRows(Arrays.asList(department, department2));
    assertEquals(2, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("foo", department.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department.getVersion());

    department2 = dao.selectById(100);
    assertEquals(Integer.valueOf(100), department2.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(100), department2.getDepartmentNo());
    assertEquals("bar", department2.getDepartmentName());
    assertEquals("boo", department2.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department2.getVersion());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testImmutable(Config config) throws Exception {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(99), 99, "hoge", new Location<>("foo"), null);
    Dept dept2 = new Dept(new Identity<>(100), 100, "bar", new Location<>("baz"), null);

    MultiResult<Dept> result = dao.insertMultiRows(Arrays.asList(dept, dept2));
    assertEquals(2, result.getCount());

    List<Dept> deptList = result.getEntities();
    dept = deptList.get(0);
    dept2 = deptList.get(1);

    assertEquals(Integer.valueOf(1), dept.getVersion());
    assertEquals("hoge_preI(E)_postI(E)", dept.getDepartmentName());

    assertEquals(Integer.valueOf(1), dept2.getVersion());
    assertEquals("bar_preI(E)_postI(E)", dept2.getDepartmentName());

    dept = dao.selectById(99);
    assertEquals(Integer.valueOf(99), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), dept.getDepartmentNo());
    assertEquals("hoge_preI(E)", dept.getDepartmentName());
    assertEquals("foo", dept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), dept.getVersion());

    dept2 = dao.selectById(100);
    assertEquals(Integer.valueOf(100), dept2.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(100), dept2.getDepartmentNo());
    assertEquals("bar_preI(E)", dept2.getDepartmentName());
    assertEquals("baz", dept2.getLocation().getValue());
    assertEquals(Integer.valueOf(1), dept2.getVersion());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void test_UniqueConstraintException(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<Department>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insertMultiRows(Collections.singletonList(department));
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());
    try {
      dao.insertMultiRows(Collections.singletonList(department));
      fail();
    } catch (UniqueConstraintException expected) {
    }
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testCompositeKey(Config config) throws Exception {
    CompKeyDepartmentDao dao = new CompKeyDepartmentDaoImpl(config);

    CompKeyDepartment department = new CompKeyDepartment();
    department.setDepartmentId1(99);
    department.setDepartmentId2(99);
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");

    CompKeyDepartment department2 = new CompKeyDepartment();
    department2.setDepartmentId1(100);
    department2.setDepartmentId2(100);
    department2.setDepartmentNo(100);
    department2.setDepartmentName("foo");

    int result = dao.insertMultiRows(Arrays.asList(department, department2));
    assertEquals(2, result);
    assertEquals(Integer.valueOf(1), department.getVersion());
    assertEquals(Integer.valueOf(1), department2.getVersion());

    department = dao.selectById(99, 99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId1());
    assertEquals(Integer.valueOf(99), department.getDepartmentId2());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertNull(department.getLocation());
    assertEquals(Integer.valueOf(1), department.getVersion());

    department2 = dao.selectById(100, 100);
    assertEquals(Integer.valueOf(100), department2.getDepartmentId1());
    assertEquals(Integer.valueOf(100), department2.getDepartmentId2());
    assertEquals(Integer.valueOf(100), department2.getDepartmentNo());
    assertEquals("foo", department2.getDepartmentName());
    assertNull(department2.getLocation());
    assertEquals(Integer.valueOf(1), department2.getVersion());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testIdNotAssigned(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    try {
      dao.insertMultiRows(Collections.singletonList(department));
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2020, expected.getMessageResource());
    }
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void testId_Identity(Config config) throws Exception {
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(config);
    IdentityStrategy entity1 = new IdentityStrategy();
    IdentityStrategy entity2 = new IdentityStrategy();
    IdentityStrategy entity3 = new IdentityStrategy();
    int result = dao.insertMultiRows(Arrays.asList(entity1, entity2, entity3));

    assertEquals(3, result);
    assertNotNull(entity1.getId());
    assertNotNull(entity2.getId());
    assertNotNull(entity3.getId());
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void testId_PrimitiveIdentity(Config config) throws Exception {
    PrimitiveIdentityStrategyDao dao = new PrimitiveIdentityStrategyDaoImpl(config);
    List<PrimitiveIdentityStrategy> entities = new ArrayList<>(110);
    for (int i = 0; i < 110; i++) {
      PrimitiveIdentityStrategy entity = new PrimitiveIdentityStrategy();
      entities.add(entity);
    }
    dao.insertMultiRows(entities);
    for (PrimitiveIdentityStrategy entity : entities) {
      assertTrue(entity.getId() > 0);
    }
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE, Dbms.SQLSERVER})
  public void testId_sequence(Config config) throws Exception {
    SequenceStrategyDao dao = new SequenceStrategyDaoImpl(config);
    SequenceStrategy entity1 = new SequenceStrategy();
    SequenceStrategy entity2 = new SequenceStrategy();
    SequenceStrategy entity3 = new SequenceStrategy();
    int result = dao.insertMultiRows(Arrays.asList(entity1, entity2, entity3));

    assertEquals(3, result);
    assertNotNull(entity1.getId());
    assertNotNull(entity2.getId());
    assertNotNull(entity3.getId());
  }

  // it seems that sqlite doesn't support requiresNew transaction
  // so ignore this test case
  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLITE})
  public void testId_table(Config config) throws Exception {
    TableStrategyDao dao = new TableStrategyDaoImpl(config);
    TableStrategy entity1 = new TableStrategy();
    TableStrategy entity2 = new TableStrategy();
    TableStrategy entity3 = new TableStrategy();
    int result = dao.insertMultiRows(Arrays.asList(entity1, entity2, entity3));

    assertEquals(3, result);
    assertNotNull(entity1.getId());
    assertNotNull(entity2.getId());
    assertNotNull(entity3.getId());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testNoId(Config config) throws Exception {
    NoIdDao dao = new NoIdDaoImpl(config);

    NoId entity1 = new NoId();
    entity1.setValue1(1);
    entity1.setValue2(2);

    NoId entity2 = new NoId();
    entity2.setValue1(3);
    entity2.setValue2(4);

    NoId entity3 = new NoId();
    entity3.setValue1(5);
    entity3.setValue2(6);

    int result = dao.insertMultiRows(Arrays.asList(entity1, entity2, entity3));

    assertEquals(3, result);
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testOptional(Config config) throws Exception {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker worker = new Worker();
    worker.employeeId = Optional.of(9999);
    worker.employeeNo = Optional.of(9999);

    int result = dao.insertMultiRows(Collections.singletonList(worker));
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
  @Run(unless = {Dbms.ORACLE})
  public void testOptionalInt(Config config) throws Exception {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman worker = new Businessman();
    worker.employeeId = OptionalInt.of(9999);
    worker.employeeNo = OptionalInt.of(9999);
    int result = dao.insertMultiRows(Collections.singletonList(worker));
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
  @Run(onlyIf = {Dbms.H2, Dbms.MYSQL, Dbms.MYSQL8, Dbms.POSTGRESQL, Dbms.SQLSERVER})
  public void testEmbeddable(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 9999;
    staff.employeeNo = 9999;
    staff.staffInfo = new StaffInfo(13, Date.valueOf("2016-05-27"), new Salary("1234"));
    int result = dao.insertMultiRows(Collections.singletonList(staff));
    assertEquals(1, result);
    assertEquals(1, staff.version.intValue());

    staff = dao.selectById(9999);
    StaffInfo staffInfo = staff.staffInfo;
    assertNotNull(staffInfo);
    assertEquals(Date.valueOf("2016-05-27"), staffInfo.hiredate);
    assertEquals(1234L, staffInfo.salary.getValue().longValue());
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testEmbeddable_null(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 9999;
    staff.employeeNo = 9999;
    int result = dao.insertMultiRows(Collections.singletonList(staff));
    assertEquals(1, result);
    assertEquals(1, staff.version.intValue());

    staff = dao.selectById(9999);
    StaffInfo staffInfo = staff.staffInfo;
    assertNotNull(staffInfo);
    assertNull(staffInfo.hiredate);
    assertNull(staffInfo.salary);
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testNestedEntity(Config config) throws Exception {
    BranchDao dao = new BranchDaoImpl(config);
    {
      Branch branch = new Branch();
      branch.branchId = 99;
      branch.branchDetail = new BranchDetail(99, "hoge", new BranchDao.Location("foo"));
      dao.insertMultiRows(Collections.singletonList(branch));
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
      BranchDao.Location location = branchDetail.location;
      assertNotNull(location);
      assertEquals("foo", location.getValue());
    }
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void whenListIsEmpty(Config config) throws Exception {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    int result = dao.insertMultiRows(Collections.emptyList());
    assertEquals(0, result);
  }
}