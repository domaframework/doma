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
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.TimeDomain;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;

@Dao(config = ItConfig.class)
public interface ProcedureDao {

    @Procedure
    void proc_none_param();

    @Procedure
    void proc_simpletype_param(@In IntegerDomain param1);

    @Procedure
    void proc_simpletype_time_param(@In TimeDomain param1);

    @Procedure
    void proc_dto_param(@In IntegerDomain param1, @InOut IntegerDomain param2,
            @Out IntegerDomain param3);

    @Procedure
    void proc_dto_time_param(@In TimeDomain param1, @InOut TimeDomain param2,
            @Out TimeDomain param3);

    @Procedure
    void proc_resultset(@ResultSet List<Employee> employees,
            @In IntegerDomain employee_id);

    @Procedure
    void proc_resultset_out(@ResultSet List<Employee> employees,
            @In IntegerDomain employee_id, @Out IntegerDomain count);

    @Procedure
    void proc_resultset_update(@ResultSet List<Employee> employees,
            @In IntegerDomain employee_id);

    @Procedure
    void proc_resultset_update2(@ResultSet List<Employee> employees,
            @In IntegerDomain employee_id);

    @Procedure
    void proc_resultsets(@ResultSet List<Employee> employees,
            @ResultSet List<Department> departments,
            @In IntegerDomain employee_id, @In IntegerDomain department_id);

    @Procedure
    void proc_resultsets_updates_out(@ResultSet List<Employee> employees,
            @ResultSet List<Department> departments,
            @In IntegerDomain employee_id, @In IntegerDomain department_id,
            @Out IntegerDomain count);
}
