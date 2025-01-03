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
