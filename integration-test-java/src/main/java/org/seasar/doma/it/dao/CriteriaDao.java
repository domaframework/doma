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

import java.util.List;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.it.entity.Department_;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@Dao
public interface CriteriaDao {

  default List<Employee> selectAll() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    QueryDsl dsl = new QueryDsl(Config.get(this));
    return dsl.from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .fetch();
  }

  default Optional<Employee> selectById(Integer id) {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    QueryDsl dsl = new QueryDsl(Config.get(this));
    return dsl.from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .where(c -> c.eq(e.employeeId, id))
        .fetchOptional();
  }
}
