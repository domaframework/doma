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
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

/**
 * @author taedium
 * 
 */
@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface DeptDao {

    static DeptDao get() {
        return new DeptDaoImpl();
    }

    @Select
    Dept selectById(Integer departmentId);

    @Insert
    Result<Dept> insert(Dept d);

    @Update
    Result<Dept> update(Dept d);

    @BatchInsert
    BatchResult<Dept> insert(List<Dept> entity);

    @BatchUpdate
    BatchResult<Dept> update(List<Dept> entity);

    @Insert(sqlFile = true)
    Result<Dept> insertBySqlFile(Dept entity);

    @Update(sqlFile = true)
    Result<Dept> updateBySqlFile(Dept entity);

    @BatchInsert(sqlFile = true)
    BatchResult<Dept> insertBySqlFile(List<Dept> entity);

    @BatchUpdate(sqlFile = true)
    BatchResult<Dept> updateBySqlFile(List<Dept> entity);

}
