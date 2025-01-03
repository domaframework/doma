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
package org.seasar.doma.it.other;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
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
  public void testSelectBySingleExternalDomain(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employee = dao.selectByHiredate(new HiredateImpl(Date.valueOf("1980-12-17")));
    assertEquals(1, employee.size());
  }

  @Test
  public void testSelectByExternalDomainList(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Hiredate date = new HiredateImpl(Date.valueOf("1980-12-17"));
    List<Employee> employee = dao.selectByHiredates(List.of(date));
    assertEquals(1, employee.size());
  }
}
