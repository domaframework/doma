/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Department;

@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface DepartmentDao {

    static DepartmentDao get() {
        return new DepartmentDaoImpl();
    }

    @Select
    Department selectById(Integer departmentId);

    @Insert
    int insert(Department entity);

    @Insert(sqlFile = true)
    int insertBySqlFile(Department entity);

    @Insert(excludeNull = true)
    int insert_excludeNull(Department entity);

    @Update
    int update(Department entity);

    @Update(sqlFile = true)
    int updateBySqlFile(Department entity);

    @Update(sqlFile = true, ignoreVersion = true)
    int updateBySqlFile_ignoreVersion(Department entity);

    @Update(excludeNull = true)
    int update_excludeNull(Department entity);

    @Update(ignoreVersion = true)
    int update_ignoreVersion(Department entity);

    @Update(suppressOptimisticLockException = true)
    int update_suppressOptimisticLockException(Department entity);

    @BatchInsert
    int[] insert(List<Department> entity);

    @BatchInsert(sqlFile = true)
    int[] insertBySqlFile(List<Department> entity);

    @BatchUpdate
    int[] update(List<Department> entity);

    @BatchUpdate(sqlFile = true)
    int[] updateBySqlFile(List<Department> entity);

    @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
    int[] updateBySqlFile_suppressOptimisticLockException(
            List<Department> entity);

    @BatchUpdate(ignoreVersion = true)
    int[] update_ignoreVersion(List<Department> entity);

    @BatchUpdate(suppressOptimisticLockException = true)
    int[] update_suppressOptimisticLockException(List<Department> entity);
}
