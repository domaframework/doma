package org.seasar.doma.it.other;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.domain.Hiredate;
import org.seasar.doma.it.domain.HiredateImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class ExternalDomainTest {

  @Test
  public void testSelectBySingleExternalDomain(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employee = dao.selectByHiredate(new HiredateImpl(Date.valueOf("1980-12-17")));
    assertEquals(1, employee.size());
  }

  @Test
  public void testSelectByExternalDomainList(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Hiredate date = new HiredateImpl(Date.valueOf("1980-12-17"));
    List<Employee> employee = dao.selectByHiredates(Arrays.asList(date));
    assertEquals(1, employee.size());
  }
}
