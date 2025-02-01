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
import java.util.function.BiFunction;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;
import org.seasar.doma.it.entity.Address;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface DepartmentDao {

  @Select
  Department selectById(Integer departmentId);

  @Insert
  int insert(Department entity);

  @Insert(sqlFile = true)
  int insertBySqlFile(Department entity);

  @Insert(excludeNull = true)
  int insert_excludeNull(Department entity);

  @MultiInsert
  int insertMultiRows(List<Department> entities);

  @Update
  int update(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFile(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFileWithPopulate(Department entity);

  @Update(sqlFile = true, ignoreVersion = true)
  int updateBySqlFile_ignoreVersion(Department entity);

  @Update(excludeNull = true)
  int update_excludeNull(Department entity);

  @Update(ignoreVersion = true)
  int update_ignoreVersion(Department entity);

  @Update(suppressOptimisticLockException = true)
  int update_suppressOptimisticLockException(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFile_nonEntity(
      Identity<Department> departmentId,
      Integer departmentNo,
      String departmentName,
      Location<Department> location,
      Integer version);

  @BatchInsert
  int[] insert(List<Department> entity);

  @BatchInsert(sqlFile = true)
  int[] insertBySqlFile(List<Department> entity);

  @BatchUpdate
  int[] update(List<Department> entity);

  @BatchUpdate(sqlFile = true)
  int[] updateBySqlFile(List<Department> entity);

  @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
  int[] updateBySqlFile_suppressOptimisticLockException(List<Department> entity);

  @BatchUpdate(ignoreVersion = true)
  int[] update_ignoreVersion(List<Department> entity);

  @BatchUpdate(suppressOptimisticLockException = true)
  int[] update_suppressOptimisticLockException(List<Department> entity);

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int insertOnDuplicateKeyUpdate(Department entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int[] insertOnDuplicateKeyUpdate(List<Department> entities);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOnDuplicateKeyIgnore(Department entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int[] insertOnDuplicateKeyIgnore(List<Department> entities);

  @Select(aggregateStrategy = DepartmentStrategy.class)
  Department selectByIdAsAggregate(Integer departmentId);

  @Select(aggregateStrategy = DepartmentStrategy.class)
  List<Department> selectAllAsAggregate();

  @AggregateStrategy(root = Department.class, tableAlias = "d")
  interface DepartmentStrategy {
    @AssociationLinker(propertyPath = "employeeList", tableAlias = "e")
    BiFunction<Department, Employee, Department> employeeList =
        (d, e) -> {
          d.getEmployeeList().add(e);
          e.setDepartment(d);
          return d;
        };

    @AssociationLinker(propertyPath = "employeeList.address", tableAlias = "a")
    BiFunction<Employee, Address, Employee> employeeListAddress =
        (e, a) -> {
          e.setAddress(a);
          return e;
        };
  }
}
