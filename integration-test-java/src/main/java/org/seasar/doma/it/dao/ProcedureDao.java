package org.seasar.doma.it.dao;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Reference;

@Dao
public interface ProcedureDao {

  @Procedure
  void proc_none_param();

  @Procedure
  void proc_simpletype_param(@In Integer param1);

  @Procedure
  void proc_simpletype_time_param(@In Time param1);

  @Procedure
  void proc_dto_param(
      @In Integer param1, @InOut Reference<Integer> param2, @Out Reference<Integer> param3);

  @Procedure
  void proc_dto_time_param(
      @In Time param1, @InOut Reference<Time> param2, @Out Reference<Time> param3);

  @Procedure
  void proc_resultset(@ResultSet List<Employee> employees, @In Integer employee_id);

  @Procedure(name = "proc_resultset")
  void proc_resultset_check(
      @ResultSet(ensureResultMapping = true) List<Employee> employees, @In Integer employee_id);

  @Procedure(name = "proc_resultset")
  void proc_resultset_nocheck(
      @ResultSet(ensureResultMapping = false) List<Employee> employees, @In Integer employee_id);

  @Procedure(name = "proc_resultset", mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  void proc_resultset_map(@ResultSet List<Map<String, Object>> employees, @In Integer employee_id);

  @Procedure
  void proc_resultset_out(
      @ResultSet List<Employee> employees, @In Integer employee_id, @Out Reference<Integer> count);

  @Procedure
  void proc_resultset_update(@ResultSet List<Employee> employees, @In Integer employee_id);

  @Procedure
  void proc_resultset_update2(@ResultSet List<Employee> employees, @In Integer employee_id);

  @Procedure
  void proc_resultsets(
      @ResultSet List<Employee> employees,
      @ResultSet List<Department> departments,
      @In Integer employee_id,
      @In Integer department_id);

  @Procedure
  void proc_resultsets_updates_out(
      @ResultSet List<Employee> employees,
      @ResultSet List<Department> departments,
      @In Integer employee_id,
      @In Integer department_id,
      @Out Reference<Integer> count);
}
