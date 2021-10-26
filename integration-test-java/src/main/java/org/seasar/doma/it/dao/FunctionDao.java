package org.seasar.doma.it.dao;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.it.entity.Employee;

@Dao
public interface FunctionDao {

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
  List<String> func_simpletype_resultset(@In Integer employee_id);

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
