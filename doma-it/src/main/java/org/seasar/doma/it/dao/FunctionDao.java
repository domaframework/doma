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
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.TimeDomain;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;

@Dao(config = ItConfig.class)
public interface FunctionDao {

    @Function
    IntegerDomain func_none_param();

    @Function
    IntegerDomain func_simpletype_param(@In IntegerDomain param1);

    @Function
    TimeDomain func_simpletype_time_param(@In TimeDomain param1);

    @Function
    IntegerDomain func_dto_param(@In IntegerDomain param1,
            @In IntegerDomain param2);

    @Function
    TimeDomain func_dto_time_param(@In TimeDomain param1,
            @In IntegerDomain param2);

    @Function
    List<Employee> func_resultset(@In IdDomain employee_id);

    @Function
    List<Employee> func_resultset_update(@In IdDomain employee_id);

    @Function
    List<Employee> func_resultset_update2(@In IdDomain employee_id);

}
