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
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface PersonDao {

    static PersonDao get() {
        return new PersonDaoImpl();
    }

    @Select
    Person selectById(Integer employeeId);

    @Delete
    Result<Person> delete(Person entity);

    @BatchDelete
    BatchResult<Person> delete(List<Person> entity);

    @Delete(sqlFile = true)
    Result<Person> deleteBySqlFile(Person entity);

    @BatchDelete(sqlFile = true)
    BatchResult<Person> deleteBySqlFile(List<Person> entity);

}
