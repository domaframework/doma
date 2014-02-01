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
package org.seasar.doma.it.dao;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Worker;

/**
 * @author nakamura-to
 *
 */
@Dao(config = ItConfig.class)
public interface WorkerDao {

    public static WorkerDao get() {
        return new WorkerDaoImpl();
    }

    @Select
    List<Worker> selectAll();

    @Select
    Worker selectById(Optional<Integer> id);

    @Select
    List<Worker> selectByExample(Worker worker);

    @Insert
    int insert(Worker entity);

    @Update
    int update(Worker entity);

    @Delete
    int delete(Worker entity);

    @BatchInsert
    int[] insert(List<Worker> entity);

    @BatchUpdate
    int[] update(List<Worker> entity);

    @BatchDelete
    int[] delete(List<Worker> entity);

}
