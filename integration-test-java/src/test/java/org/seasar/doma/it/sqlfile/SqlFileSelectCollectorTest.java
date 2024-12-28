package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileSelectCollectorTest {

  @Test
  public void testCollectAll(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count = dao.collectAll(Collectors.counting());
    assertEquals(Long.valueOf(14), count);
  }

  @Test
  public void testCollectAll2(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Map<Identity<Department>, List<Employee>> group =
        dao.collectAll(Collectors.groupingBy(Employee::getDepartmentId));
    System.out.println(group);
  }
}
