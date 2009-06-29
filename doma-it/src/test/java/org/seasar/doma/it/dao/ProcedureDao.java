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
