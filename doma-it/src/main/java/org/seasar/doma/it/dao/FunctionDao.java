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

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinTimeDomain;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;

@Dao(config = ItConfig.class)
public interface FunctionDao {

    @Function
    BuiltinIntegerDomain func_none_param();

    @Function
    BuiltinIntegerDomain func_simpletype_param(@In BuiltinIntegerDomain param1);

    @Function
    BuiltinTimeDomain func_simpletype_time_param(@In BuiltinTimeDomain param1);

    @Function
    BuiltinIntegerDomain func_dto_param(@In BuiltinIntegerDomain param1,
            @In BuiltinIntegerDomain param2);

    @Function
    BuiltinTimeDomain func_dto_time_param(@In BuiltinTimeDomain param1,
            @In BuiltinIntegerDomain param2);

    @Function
    List<Employee> func_resultset(@In IdDomain employee_id);

    @Function
    List<Employee> func_resultset_update(@In IdDomain employee_id);

    @Function
    List<Employee> func_resultset_update2(@In IdDomain employee_id);

}
