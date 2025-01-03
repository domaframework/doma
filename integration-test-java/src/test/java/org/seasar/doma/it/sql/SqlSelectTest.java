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
package org.seasar.doma.it.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.DelegatingConfig;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlBuilderSettings;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlSelectTest {

  @Test
  public void inList(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> list = dao.selectByNames(List.of("SMITH", "KING", "WARD"));
    assertEquals(3, list.size());
  }

  @Test
  public void inList_padding(Config config) {
    Config newConfig =
        new DelegatingConfig(config) {

          @Override
          public SqlBuilderSettings getSqlBuilderSettings() {
            return new SqlBuilderSettings() {

              @Override
              public boolean requiresInListPadding() {
                return true;
              }
            };
          }
        };
    EmployeeDao dao = new EmployeeDaoImpl(newConfig);
    List<Employee> list = dao.selectByNames(List.of("SMITH", "KING", "WARD"));
    assertEquals(3, list.size());
  }
}
