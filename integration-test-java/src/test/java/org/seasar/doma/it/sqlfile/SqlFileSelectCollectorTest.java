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
  public void testCollectAll(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count = dao.collectAll(Collectors.counting());
    assertEquals(Long.valueOf(14), count);
  }

  @Test
  public void testCollectAll2(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Map<Identity<Department>, List<Employee>> group =
        dao.collectAll(Collectors.groupingBy(Employee::getDepartmentId));
    System.out.println(group);
  }

  //
  // @Test
  // public void testStreamBySalary(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // Long count = dao.streamBySalary(new BigDecimal(2000),
  // stream -> stream.count());
  // assertEquals(Long.valueOf(6), count);
  // }
  //
  // @Test
  // public void testEntity(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // long count = dao.streamAll(s -> s.count());
  // assertEquals(14L, count);
  // }
  //
  // @Test
  // public void testEntity_limitOffset(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // long count = dao.streamAll(s -> s.count(), SelectOptions.get().limit(5)
  // .offset(3));
  // assertEquals(5L, count);
  // }
  //
  // @Test
  // public void testDomain(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // BigDecimal total = dao.streamAllSalary(s -> s.filter(Objects::nonNull)
  // .reduce(BigDecimal.ZERO, (x, y) -> x.add(y)));
  // assertTrue(new BigDecimal("29025").compareTo(total) == 0);
  // }
  //
  // @Test
  // public void testDomain_limitOffset(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // BigDecimal total = dao.streamAllSalary(s -> s.filter(Objects::nonNull)
  // .reduce(BigDecimal.ZERO, (x, y) -> x.add(y)), SelectOptions
  // .get().limit(5).offset(3));
  // assertTrue(new BigDecimal("6900").compareTo(total) == 0);
  // }
  //
  // @Test
  // public void testMap(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // long count = dao.selectAllAsMapList(s -> s.count());
  // assertEquals(14L, count);
  // }
  //
  // @Test
  // public void testMap_limitOffset(Config config) throws Exception {
  // EmployeeDao dao = new EmployeeDaoImpl(config);
  // long count = dao.selectAllAsMapList(s -> s.count(), SelectOptions.get()
  // .limit(5).offset(3));
  // assertEquals(5L, count);
  // }
}
