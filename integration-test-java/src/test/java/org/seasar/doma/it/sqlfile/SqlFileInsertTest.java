package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileInsertTest {

  @Test
  public void test(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = new Department();
    department.setDepartmentId(new Identity<>(99));
    department.setDepartmentNo(99);
    department.setDepartmentName("hoge");
    int result = dao.insertBySqlFile(department);
    assertEquals(1, result);

    department = dao.selectById(99);
    assertEquals(Integer.valueOf(99), department.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), department.getDepartmentNo());
  }

  @Test
  public void testImmutable(Config config) {
    DeptDao dao = new DeptDaoImpl(config);
    Dept dept = new Dept(new Identity<>(99), 99, "hoge", null, null);
    Result<Dept> result = dao.insertBySqlFile(dept);
    assertEquals(1, result.getCount());
    dept = result.getEntity();
    assertEquals("hoge_preI(E)_postI(E)", dept.getDepartmentName());

    dept = dao.selectById(99);
    assertEquals(Integer.valueOf(99), dept.getDepartmentId().getValue());
    assertEquals(Integer.valueOf(99), dept.getDepartmentNo());
    assertEquals("hoge_preI(E)", dept.getDepartmentName());
  }
}
