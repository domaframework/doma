package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;

import doma.Dao;
import doma.Function;
import doma.In;
import doma.domain.IntegerDomain;
import doma.domain.TimeDomain;

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
