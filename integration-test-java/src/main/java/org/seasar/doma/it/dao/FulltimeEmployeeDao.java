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
package org.seasar.doma.it.dao;

import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.it.domain.Money;
import org.seasar.doma.it.entity.FulltimeEmployee;

@Dao
public interface FulltimeEmployeeDao {

  @Sql("select salary from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Money selectSalaryById(int id);

  @Sql("select salary from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Optional<Money> selectOptionalSalaryById(int id);

  @Insert
  int insert(FulltimeEmployee e);
}
