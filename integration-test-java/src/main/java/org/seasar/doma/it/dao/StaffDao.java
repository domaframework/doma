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
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Staff;

@Dao
public interface StaffDao {

  @Select
  Staff selectById(Integer employeeId);

  @Insert
  int insert(Staff staff);

  @Update
  int update(Staff staff);

  @Update(include = "staffInfo.salary")
  int updateSalary(Staff staff);

  @Update(exclude = "staffInfo.salary")
  int updateExceptSalary(Staff staff);

  @Update(sqlFile = true)
  int updateBySqlFile(Staff staff);

  @Delete
  int delete(Staff staff);

  @BatchInsert
  int[] insert(List<Staff> staff);

  @BatchUpdate
  int[] update(List<Staff> staff);

  @BatchDelete
  int[] delete(List<Staff> staff);

  @MultiInsert
  int insertMultiRows(List<Staff> entities);
}
