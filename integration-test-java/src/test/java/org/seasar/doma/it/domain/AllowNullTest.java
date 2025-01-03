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
package org.seasar.doma.it.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.FulltimeEmployeeDao;
import org.seasar.doma.it.dao.FulltimeEmployeeDaoImpl;
import org.seasar.doma.it.entity.FulltimeEmployee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class AllowNullTest {

  @Test
  void selectDomain(Config config) {
    FulltimeEmployeeDao dao = new FulltimeEmployeeDaoImpl(config);
    FulltimeEmployee e = new FulltimeEmployee();
    e.setEmployeeId(99);
    e.setEmployeeNo(9999);
    dao.insert(e);

    Money salary = dao.selectSalaryById(99);
    assertNotNull(salary);
    assertNull(salary.getValue());
  }

  @Test
  void selectOptionalDomain(Config config) {
    FulltimeEmployeeDao dao = new FulltimeEmployeeDaoImpl(config);
    FulltimeEmployee e = new FulltimeEmployee();
    e.setEmployeeId(99);
    e.setEmployeeNo(9999);
    dao.insert(e);

    Optional<Money> salary = dao.selectOptionalSalaryById(99);
    assertNotNull(salary);
    assertFalse(salary.isPresent());
  }
}
