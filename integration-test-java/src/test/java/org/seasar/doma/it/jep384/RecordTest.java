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
package org.seasar.doma.it.jep384;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
public class RecordTest {

  @Test
  void selectAll(Config config) {
    var dao = new RecordDaoImpl(config);
    var list = dao.selectAll();
    Assertions.assertEquals(4, list.size());
  }

  @Test
  void selectById(Config config) {
    var dao = new RecordDaoImpl(config);
    var department = dao.selectById(1);
    assertNotNull(department);
  }

  @Test
  void criteria_select(Config config) {
    var dsl = new QueryDsl(config);
    var w = new Worker_();
    var list = dsl.from(w).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void criteria_insert(Config config) {
    var dsl = new QueryDsl(config);
    var w = new Worker_();
    var worker =
        new Worker(
            16, 9999, "aaa", new WorkerInfo(13, LocalDate.now()), Salary.of("1000"), 1, 1, null);
    dsl.insert(w).single(worker).execute();
    var newWorker = dsl.from(w).where(c -> c.eq(w.employeeId, 16)).fetchOne();
    assertNotNull(newWorker);
  }

  @Test
  void criteria_update(Config config) {
    var dsl = new QueryDsl(config);
    var w = new Worker_();
    var worker = dsl.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
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
    dsl.update(w).single(worker2).execute();
    var worker3 = dsl.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    assertNotNull(worker3);
    var info = worker3.workerInfo();
    assertNotNull(info);
    assertEquals(2, info.managerId());
    var salary = worker3.salary();
    assertEquals(0, salary.value().compareTo(new BigDecimal("1000")));
  }

  @Test
  void criteria_delete(Config config) {
    var dsl = new QueryDsl(config);
    var w = new Worker_();
    var worker = dsl.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    dsl.delete(w).single(worker).execute();
    var worker2 = dsl.from(w).where(c -> c.eq(w.employeeId, 1)).fetchOne();
    assertNull(worker2);
  }
}
