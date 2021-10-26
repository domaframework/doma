package org.seasar.doma.it.jep384;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;

@ExtendWith(IntegrationTestEnvironment.class)
public class RecordTest {

  @Test
  void selectAll(Config config) {
    var dao = new RecordDaoImpl(config);
    var list = dao.selectAll();
    assertEquals(4, list.size());
  }

  @Test
  void selectById(Config config) {
    var dao = new RecordDaoImpl(config);
    var department = dao.selectById(1);
    assertNotNull(department);
  }

  @Test
  void criteria_select(Config config) {
    var entityql = new Entityql(config);
    var w = new Worker_();
    var list = entityql.from(w).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void criteria_insert(Config config) {
    var entityql = new Entityql(config);
    var w = new Worker_();
    var worker =
        new Worker(
            16, 9999, "aaa", new WorkerInfo(13, LocalDate.now()), Salary.of("1000"), 1, 1, null);
    entityql.insert(w, worker).execute();
    var newWorker = entityql.from(w).where(c -> c.eq(w.employeeId, 16)).fetchOne();
    assertNotNull(newWorker);
  }

  @Test
  void criteria_update(Config config) {
    var entityql = new Entityql(config);
    var w = new Worker_();
    var worker = entityql.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    var worker2 =
        new Worker(
            worker.employeeId(),
            worker.employeeNo(),
            worker.employeeName(),
            new WorkerInfo(2, LocalDate.now()),
            new Salary(new BigDecimal("1000")),
            worker.departmentId(),
            worker.addressId(),
            worker.version());
    entityql.update(w, worker2).execute();
    var worker3 = entityql.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    assertNotNull(worker3);
    var info = worker3.workerInfo();
    assertNotNull(info);
    assertEquals(2, info.managerId());
    var salary = worker3.salary();
    assertEquals(0, salary.value().compareTo(new BigDecimal("1000")));
  }

  @Test
  void criteria_delete(Config config) {
    var entityql = new Entityql(config);
    var w = new Worker_();
    var worker = entityql.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    entityql.delete(w, worker).execute();
    var worker2 = entityql.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    assertNull(worker2);
  }
}
