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

import org.seasar.doma.Dao;
import org.seasar.doma.Returning;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Staff;

@Dao
public interface UpdateReturningDao {

  @Sql("select * from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Employee selectById(Integer id);

  @Update(returning = @Returning)
  Employee updateThenReturnAll(Employee employee);

  @Update(returning = @Returning(include = "employeeId"))
  Employee updateThenReturnOnlyId(Employee employee);

  @Update(returning = @Returning(exclude = "version"))
  Employee updateThenReturnExceptVersion(Employee employee);

  @Sql("select * from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Staff selectStaffById(Integer id);

  @Update(returning = @Returning(include = {"staffInfo.managerId", "staffInfo.salary"}))
  Staff updateStaffThenReturnManagerAndSalary(Staff staff);

  @Update(returning = @Returning(exclude = "staffInfo.salary"))
  Staff updateStaffThenReturnExceptSalary(Staff staff);
}
