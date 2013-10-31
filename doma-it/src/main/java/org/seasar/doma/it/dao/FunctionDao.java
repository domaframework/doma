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

import java.sql.Time;
import java.util.List;
import java.util.Map;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Employee;

@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface FunctionDao {

    static FunctionDao get() {
        return new FunctionDaoImpl();
    }

    @Function
    Integer func_none_param();

    @Function
    Integer func_simpletype_param(@In Integer param1);

    @Function
    Time func_simpletype_time_param(@In Time param1);

    @Function
    Integer func_dto_param(@In Integer param1, @In Integer param2);

    @Function
    Time func_dto_time_param(@In Time param1, @In Integer param2);

    @Function
    List<Employee> func_resultset(@In Integer employee_id);

    @Function(name = "func_resultset", ensureResultMapping = true)
    List<Employee> func_resultset_check(@In Integer employee_id);

    @Function(name = "func_resultset", ensureResultMapping = false)
    List<Employee> func_resultset_nocheck(@In Integer employee_id);

    @Function(name = "func_resultset", mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    List<Map<String, Object>> func_resultset_map(@In Integer employee_id);

    @Function
    List<Employee> func_resultset_update(@In Integer employee_id);

    @Function
    List<Employee> func_resultset_update2(@In Integer employee_id);

}
