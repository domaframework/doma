package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileSelectStreamTest {

  @Test
  public void testStreamAll(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count =
        dao.streamAll(
            stream ->
                stream
                    .filter(e -> e.getEmployeeName() != null)
                    .filter(e -> e.getEmployeeName().startsWith("S"))
                    .count());
    assertEquals(Long.valueOf(2), count);
  }

  @Test
  public void testStreamAll_resultStream(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count;
    try (Stream<Employee> stream = dao.streamAll()) {
      count =
          stream
              .filter(e -> e.getEmployeeName() != null)
              .filter(e -> e.getEmployeeName().startsWith("S"))
              .count();
    }
    assertEquals(Long.valueOf(2), count);
  }

  @Test
  public void testStreamBySalary(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count = dao.streamBySalary(new BigDecimal(2000), Stream::count);
    assertEquals(Long.valueOf(6), count);
  }

  @Test
  public void testStreamBySalary_resultStream(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Long count;
    try (Stream<Employee> stream = dao.streamBySalary(new BigDecimal(2000))) {
      count = stream.count();
    }
    assertEquals(Long.valueOf(6), count);
  }

  @Test
  public void testEntity(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    long count = dao.streamAll(Stream::count);
    assertEquals(14L, count);
  }

  @Test
  public void testEntity_limitOffset(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    long count = dao.streamAll(Stream::count, SelectOptions.get().limit(5).offset(3));
    assertEquals(5L, count);
  }

  @Test
  public void testEntity_limitOffset_resultStream(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    long count;
    try (Stream<Employee> stream = dao.streamAll(SelectOptions.get().limit(5).offset(3))) {
      count = stream.count();
    }
    assertEquals(5L, count);
  }

  @Test
  public void testDomain(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    BigDecimal total =
        dao.streamAllSalary(
            s -> s.filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertEquals(0, new BigDecimal("29025").compareTo(total));
  }

  @Test
  public void testDomain_limitOffset(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    BigDecimal total =
        dao.streamAllSalary(
            s -> s.filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add),
            SelectOptions.get().limit(5).offset(3));
    assertEquals(0, new BigDecimal("6900").compareTo(total));
  }

  @Test
  public void testMap(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    long count = dao.selectAllAsMapList(Stream::count);
    assertEquals(14L, count);
  }

  @Test
  public void testMap_limitOffset(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    long count = dao.selectAllAsMapList(Stream::count, SelectOptions.get().limit(5).offset(3));
    assertEquals(5L, count);
  }
}
