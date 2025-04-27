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
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IdentityOverridableConfig;
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
import org.seasar.doma.it.dao.CompKeyDeptDao;
import org.seasar.doma.it.dao.CompKeyDeptDaoImpl;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.DeptDaoImpl;
import org.seasar.doma.it.dao.IdentityStrategy2Dao;
import org.seasar.doma.it.dao.IdentityStrategy2DaoImpl;
import org.seasar.doma.it.dao.IdentityStrategyDao;
import org.seasar.doma.it.dao.IdentityStrategyDaoImpl;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDaoImpl;
import org.seasar.doma.it.dao.OptionalIdentityStrategyDao;
import org.seasar.doma.it.dao.OptionalIdentityStrategyDaoImpl;
import org.seasar.doma.it.dao.PrimitiveIdentityStrategyDao;
import org.seasar.doma.it.dao.PrimitiveIdentityStrategyDaoImpl;
import org.seasar.doma.it.dao.ReturningDao;
import org.seasar.doma.it.dao.ReturningDaoImpl;
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
import org.seasar.doma.it.entity.CompKeyDept;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.it.entity.IdentityStrategy2;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.OptionalIdentityStrategy;
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
  public void test(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    department.setLocation(new Location<>("foo"));
    int result = dao.insert(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("foo", department.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department.getVersion());
  }

  @Test
  public void testImmutable(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(99), 99, "hoge", new Location<>("foo"), null);
    Result<Dept> result = dao.insert(dept);
    assertEquals(1, result.getCount());
    dept = result.getEntity();
    assertEquals(Integer.valueOf(1), dept.getVersion());
    assertEquals("hoge_preI(E)_postI(E)", dept.getDepartmentName());

    dept = dao.selectById(99);
    assertEquals(Integer.valueOf(99), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), dept.getDepartmentNo());
    assertEquals("hoge_preI(E)", dept.getDepartmentName());
    assertEquals("foo", dept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), dept.getVersion());
  }

  @Test
  public void test_UniqueConstraintException_primaryKey(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
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
  public void test_UniqueConstraintException_uniqueKey(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insert(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());
    department.setDepartmentId(new Identity<>(100));
    try {
      dao.insert(department);
      fail();
    } catch (UniqueConstraintException e) {
    }
  }

  @Test
  public void testExcludeNull(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insert_excludeNull(department);
    assertEquals(1, result);
    assertEquals(Integer.valueOf(1), department.getVersion());

    department = dao.selectById(99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
    assertEquals("hoge", department.getDepartmentName());
    assertEquals("TOKYO", department.getLocation().getValue());
    assertEquals(Integer.valueOf(1), department.getVersion());
  }

  @Test
  public void testCompositeKey(Config config) {
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
  public void testIdNotAssigned(Config config) {
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
  public void testId_Identity(Config config) {
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      IdentityStrategy entity = new IdentityStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
  }

  @Test
  @Run(unless = {Dbms.SQLSERVER})
  public void testId_Identity_override(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategy entity = new IdentityStrategy();
      entity.setId(1000 + i);
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
    var expected = IntStream.rangeClosed(1000, 1109).boxed().toList();
    var actual = dao.selectAll().stream().map(IdentityStrategy::getId).sorted().toList();
    assertEquals(expected, actual);
  }

  @Test
  public void testId_Identity_dontOverride(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategy entity = new IdentityStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
    var expected = IntStream.rangeClosed(1, 110).boxed().toList();
    var actual = dao.selectAll().stream().map(IdentityStrategy::getId).sorted().toList();
    assertEquals(expected, actual);
  }

  @Test
  @Run(unless = {Dbms.SQLSERVER})
  public void testId_OptionalIdentity_override(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    OptionalIdentityStrategyDao dao = new OptionalIdentityStrategyDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      OptionalIdentityStrategy entity = new OptionalIdentityStrategy();
      entity.setId(Optional.of(1000 + i));
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
    var expected = IntStream.rangeClosed(1000, 1109).boxed().toList();
    var actual =
        dao.selectAll().stream()
            .map(OptionalIdentityStrategy::getId)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted()
            .toList();
    assertEquals(expected, actual);
  }

  @Test
  @Run(unless = {Dbms.SQLSERVER})
  public void testId_OptionalIdentity_dontOverride(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    OptionalIdentityStrategyDao dao = new OptionalIdentityStrategyDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      OptionalIdentityStrategy entity = new OptionalIdentityStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
    var expected = IntStream.rangeClosed(1, 110).boxed().toList();
    var actual =
        dao.selectAll().stream()
            .map(OptionalIdentityStrategy::getId)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted()
            .toList();
    assertEquals(expected, actual);
  }

  @Test
  public void testId_PrimitiveIdentity(Config config) {
    PrimitiveIdentityStrategyDao dao = new PrimitiveIdentityStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      PrimitiveIdentityStrategy entity = new PrimitiveIdentityStrategy();
      dao.insert(entity);
      assertTrue(entity.getId() > 0);
    }
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER, Dbms.SQLITE})
  public void testId_sequence(Config config) {
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
  public void testId_table(Config config) {
    TableStrategyDao dao = new TableStrategyDaoImpl(config);
    for (int i = 0; i < 110; i++) {
      TableStrategy entity = new TableStrategy();
      dao.insert(entity);
      assertNotNull(entity.getId());
    }
  }

  @Test
  public void testNoId(Config config) {
    NoIdDao dao = new NoIdDaoImpl(config);
    NoId entity = new NoId();
    entity.setValue1(1);
    entity.setValue2(2);
    int result = dao.insert(entity);
    assertEquals(1, result);
  }

  @Test
  public void testOptional(Config config) {
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
  public void testOptionalInt(Config config) {
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
  public void testEmbeddable(Config config) {
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
  public void testEmbeddable_null(Config config) {
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
  public void testNestedEntity(Config config) {
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

  @Test
  public void insert_DuplicateKeyType_UPDATE_nonDuplicated(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(5), 50, "PLANNING", new Location<>("TOKYO"), null);
    Result<Dept> result = dao.insertOnDuplicateKeyUpdate(dept);
    // insert result entities
    Dept resultDept = result.component1();
    assertEquals(Integer.valueOf(5), resultDept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(50), resultDept.getDepartmentNo());
    assertEquals("PLANNING_preI(U)_postI(U)", resultDept.getDepartmentName());
    assertEquals("TOKYO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    assertEquals(1, result.component2()); // inserted
    // reload from database
    Dept reloadDept = dao.selectById(dept.getDepartmentId().getValue());
    // inserted
    assertEquals(50, reloadDept.getDepartmentNo());
    assertEquals("PLANNING_preI(U)", reloadDept.getDepartmentName());
    assertEquals("TOKYO", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_UPDATE_duplicated(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(1), 60, "DEVELOPMENT", new Location<>("KYOTO"), null);
    Result<Dept> result = dao.insertOnDuplicateKeyUpdate(dept);
    // insert result entities
    Dept resultDept = result.component1();
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(60), resultDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(U)_postI(U)", resultDept.getDepartmentName());
    assertEquals("KYOTO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    // updated
    if (config.getDialect().getName().equals("mysql")
        || config.getDialect().getName().equals("mariadb")) {
      assertEquals(2, result.component2());
    } else {
      assertEquals(1, result.component2());
    }
    // reload from database
    Dept reloadDept = dao.selectById(dept.getDepartmentId().getValue());
    // updated
    assertEquals(60, reloadDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(U)", reloadDept.getDepartmentName());
    assertEquals("KYOTO", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_UPDATE_compositeKey(Config config) {
    CompKeyDeptDao dao = new CompKeyDeptDaoImpl(config);
    CompKeyDept dept =
        new CompKeyDept(
            new Identity<>(1), new Identity<>(1), 60, "DEVELOPMENT", new Location<>("KYOTO"), null);
    Result<CompKeyDept> result = dao.insertOnDuplicateKeyUpdate(dept);
    // insert result entities
    CompKeyDept resultDept = result.component1();
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId1().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId2().getValue());
    assertEquals(Integer.valueOf(60), resultDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(U)_postI(U)", resultDept.getDepartmentName());
    assertEquals("KYOTO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    // updated
    if (config.getDialect().getName().equals("mysql")
        || config.getDialect().getName().equals("mariadb")) {
      assertEquals(2, result.component2());
    } else {
      assertEquals(1, result.component2());
    }
    // reload from database
    CompKeyDept reloadDept =
        dao.selectByIds(dept.getDepartmentId1().getValue(), dept.getDepartmentId2().getValue());
    // updated
    assertEquals(60, reloadDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(U)", reloadDept.getDepartmentName());
    assertEquals("KYOTO", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_IGNORE_nonDuplicated(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(5), 50, "PLANNING", new Location<>("TOKYO"), null);
    Result<Dept> result = dao.insertOnDuplicateKeyIgnore(dept);
    // insert result entities
    Dept resultDept = result.component1();
    assertEquals(Integer.valueOf(5), resultDept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(50), resultDept.getDepartmentNo());
    assertEquals("PLANNING_preI(I)_postI(I)", resultDept.getDepartmentName());
    assertEquals("TOKYO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    assertEquals(1, result.component2()); // inserted
    // reload from database
    Dept reloadDept = dao.selectById(dept.getDepartmentId().getValue());
    // inserted
    assertEquals(50, reloadDept.getDepartmentNo());
    assertEquals("PLANNING_preI(I)", reloadDept.getDepartmentName());
    assertEquals("TOKYO", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_IGNORE_duplicated(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(1), 60, "DEVELOPMENT", new Location<>("KYOTO"), null);
    Result<Dept> result = dao.insertOnDuplicateKeyIgnore(dept);
    // insert result entities
    Dept resultDept = result.component1();
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(60), resultDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(I)_postI(I)", resultDept.getDepartmentName());
    assertEquals("KYOTO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    assertEquals(0, result.component2()); // ignored
    // reload from database
    Dept reloadDept = dao.selectById(dept.getDepartmentId().getValue());
    // ignored
    assertEquals(10, reloadDept.getDepartmentNo());
    assertEquals("ACCOUNTING", reloadDept.getDepartmentName());
    assertEquals("NEW YORK", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_IGNORE_compositeKey(Config config) {
    CompKeyDeptDao dao = new CompKeyDeptDaoImpl(config);
    CompKeyDept dept =
        new CompKeyDept(
            new Identity<>(1), new Identity<>(1), 60, "DEVELOPMENT", new Location<>("KYOTO"), null);
    Result<CompKeyDept> result = dao.insertOnDuplicateKeyIgnore(dept);
    // insert result entities
    CompKeyDept resultDept = result.component1();
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId1().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getDepartmentId2().getValue());
    assertEquals(Integer.valueOf(60), resultDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(I)_postI(I)", resultDept.getDepartmentName());
    assertEquals("KYOTO", resultDept.getLocation().getValue());
    assertEquals(Integer.valueOf(1), resultDept.getVersion());
    // insert result count
    assertEquals(0, result.component2()); // ignored
    // reload from database
    CompKeyDept reloadDept =
        dao.selectByIds(dept.getDepartmentId1().getValue(), dept.getDepartmentId2().getValue());
    // ignored
    assertEquals(10, reloadDept.getDepartmentNo());
    assertEquals("ACCOUNTING", reloadDept.getDepartmentName());
    assertEquals("NEW YORK", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8})
  public void insert_DuplicateKeyType_UPDATE_duplicated_on_specified_keys(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(2), 10, "DEVELOPMENT", new Location<>("KYOTO"), null);
    dao.insertOnDuplicateKeyUpdateWithDepartmentNo(dept);
    // reload from database
    Dept reloadDept = dao.selectByDepartmentNo(10);
    // updated
    assertEquals(1, reloadDept.getDepartmentId().getValue());
    assertEquals(10, reloadDept.getDepartmentNo());
    assertEquals("DEVELOPMENT_preI(U)", reloadDept.getDepartmentName());
    assertEquals("KYOTO", reloadDept.getLocation().getValue());
    assertEquals(1, reloadDept.getVersion());
  }

  @Test
  public void insert_DuplicateKeyType_IGNORE_identityTable_nonDuplicated(Config config) {
    IdentityStrategy2Dao dao = new IdentityStrategy2DaoImpl(config);
    var entity1 = new IdentityStrategy2();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityStrategy2();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    dao.insertOrIgnore(entity1);
    dao.insertOrIgnore(entity2);

    assertEquals(1, entity1.getId());
    assertEquals(2, entity2.getId());
    var entities = dao.selectAll();
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
  public void insert_DuplicateKeyType_IGNORE_identityTable_duplicated(Config config) {
    IdentityStrategy2Dao dao = new IdentityStrategy2DaoImpl(config);
    var entity1 = new IdentityStrategy2();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityStrategy2();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    dao.insertOrIgnore(entity1);
    dao.insertOrIgnore(entity2);

    assertEquals(1, entity1.getId());
    assertNull(entity2.getId());
    var entities = dao.selectAll();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
  }

  @Test
  public void insert_DuplicateKeyType_UPDATE_identityTable_nonDuplicated(Config config) {
    IdentityStrategy2Dao dao = new IdentityStrategy2DaoImpl(config);
    var entity1 = new IdentityStrategy2();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityStrategy2();
    entity2.setUniqueValue("2");
    entity2.setValue("B");

    dao.insertOrUpdate(entity1);
    dao.insertOrUpdate(entity2);

    if (!config.getDialect().getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
      assertEquals(2, entity2.getId());
    }
    var entities = dao.selectAll();
    assertEquals(2, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("A", entities.get(0).getValue());
    assertEquals(2, entities.get(1).getId());
    assertEquals("2", entities.get(1).getUniqueValue());
    assertEquals("B", entities.get(1).getValue());
  }

  @Test
  public void insert_DuplicateKeyType_UPDATE_identityTable_duplicated(Config config) {
    IdentityStrategy2Dao dao = new IdentityStrategy2DaoImpl(config);
    var entity1 = new IdentityStrategy2();
    entity1.setUniqueValue("1");
    entity1.setValue("A");
    var entity2 = new IdentityStrategy2();
    entity2.setUniqueValue("1");
    entity2.setValue("B");

    dao.insertOrUpdate(entity1);
    dao.insertOrUpdate(entity2);

    if (!config.getDialect().getName().equals("oracle")) {
      assertEquals(1, entity1.getId());
      assertEquals(1, entity2.getId());
    }
    var entities = dao.selectAll();
    assertEquals(1, entities.size());
    assertEquals(1, entities.get(0).getId());
    assertEquals("1", entities.get(0).getUniqueValue());
    assertEquals("B", entities.get(0).getValue());
  }

  @Test
  public void returning(Config config) {
    ReturningDao dao = new ReturningDaoImpl(config);
  }
}
