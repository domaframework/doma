package org.seasar.doma.it.other;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.dao.PhantomEmployeeDao;
import org.seasar.doma.it.dao.PhantomEmployeeDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.PhantomEmployee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
class PrimitiveTypeTest {

  @Test
  void select_primitive_types(Config config) {
    PhantomEmployeeDao dao = new PhantomEmployeeDaoImpl(config);
    Map<Integer, PhantomEmployee> map =
        dao.selectAll().stream()
            .collect(Collectors.toMap(PhantomEmployee::getEmployeeId, Function.identity()));
    assertEquals(14, map.size());

    PhantomEmployee e = map.get(1);
    assertEquals(7369f, e.getEmployeeNo());
    assertEquals((byte) 13, e.getManagerId());
    assertEquals(800d, e.getSalary().getValue());
    assertEquals(2f, e.getDepartmentId());
    assertEquals(1d, e.getAddressId());
    assertEquals(1L, e.getVersion());
  }

  @Test
  void map_null_to_primitive_type(Config config) {
    PhantomEmployeeDao dao = new PhantomEmployeeDaoImpl(config);
    PhantomEmployee e = dao.selectById(9);
    byte managerId = e.getManagerId();
    assertEquals((byte) 0, managerId);
  }

  @Test
  void map_null_to_reference_type(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee e = dao.selectById(9);
    Integer managerId = e.getManagerId();
    assertNull(managerId);
  }

  @Test
  void insert_primitive(Config config) {
    PhantomEmployeeDao dao = new PhantomEmployeeDaoImpl(config);
    {
      PhantomEmployee e = new PhantomEmployee();
      e.setEmployeeId(99);
      e.setEmployeeName("test");
      e.setDepartmentId(1);
      e.setAddressId(1);
      e.setVersion(-1);
      dao.insert(e);
    }
    {
      PhantomEmployee e = dao.selectById(99);
      assertEquals(0f, e.getEmployeeNo());
      assertEquals((byte) 0, e.getManagerId());
      assertEquals(0d, e.getSalary().getValue());
      assertEquals(1f, e.getDepartmentId());
      assertEquals(1d, e.getAddressId());
      assertEquals(1L, e.getVersion());
    }
  }

  @Test
  void insert_reference(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    {
      Employee e = new Employee();
      e.setEmployeeId(99);
      e.setEmployeeNo(0);
      e.setEmployeeName("test");
      e.setDepartmentId(new Identity<Department>(1));
      e.setAddressId(1);
      e.setVersion(-1);
      dao.insert(e);
    }
    {
      Employee e = dao.selectById(99);
      assertEquals(0, e.getEmployeeNo());
      assertNull(e.getManagerId());
      assertNull(e.getSalary());
      assertEquals(1, e.getDepartmentId().getValue());
      assertEquals(1, e.getAddressId());
      assertEquals(1, e.getVersion());
    }
  }
}
