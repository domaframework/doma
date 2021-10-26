package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.CriteriaDao;
import org.seasar.doma.it.dao.CriteriaDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class DaoTest {

  @Test
  void selectAll(Config config) {
    CriteriaDao dao = new CriteriaDaoImpl(config);
    List<Employee> list = dao.selectAll();
    assertEquals(14, list.size());
  }

  @Test
  void selectById(Config config) {
    CriteriaDao dao = new CriteriaDaoImpl(config);
    Employee employee = dao.selectById(5).orElse(null);
    assertNotNull(employee);
    assertEquals(5, employee.getEmployeeId());
    assertNotNull(employee.getDepartment());
    assertEquals(1, employee.getDepartment().getEmployeeList().size());
    assertEquals(employee, employee.getDepartment().getEmployeeList().get(0));
  }
}
