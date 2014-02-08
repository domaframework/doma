/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.apt.dao;

import java.util.List;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.entity.ImmutableEmp;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface ImmutableEmpDao {

    @Insert
    Result<ImmutableEmp> insert(ImmutableEmp emp);

    @Delete
    Result<ImmutableEmp> update(ImmutableEmp emp);

    @Update
    Result<ImmutableEmp> delete(ImmutableEmp emp);

    @BatchInsert
    BatchResult<ImmutableEmp> batchInsert(List<ImmutableEmp> emp);

    @BatchUpdate
    BatchResult<ImmutableEmp> batchUpdate(List<ImmutableEmp> emp);

    @BatchDelete
    BatchResult<ImmutableEmp> batchDelete(List<ImmutableEmp> emp);

    @Insert(sqlFile = true)
    Result<ImmutableEmp> insert2(ImmutableEmp emp);

    @Delete(sqlFile = true)
    Result<ImmutableEmp> update2(ImmutableEmp emp);

    @Update(sqlFile = true)
    Result<ImmutableEmp> delete2(ImmutableEmp emp);

    @BatchInsert(sqlFile = true)
    BatchResult<ImmutableEmp> batchInsert2(List<ImmutableEmp> emp);

    @BatchUpdate(sqlFile = true)
    BatchResult<ImmutableEmp> batchUpdate2(List<ImmutableEmp> emp);

    @BatchDelete(sqlFile = true)
    BatchResult<ImmutableEmp> batchDelete2(List<ImmutableEmp> emp);
}
