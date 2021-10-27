package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.ProcedureDao;
import org.seasar.doma.it.dao.ProcedureDaoImpl;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.ResultMappingException;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = {Dbms.HSQLDB, Dbms.H2, Dbms.SQLITE})
public class AutoProcedureTest {

  @Test
  public void testNoParam(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    dao.proc_none_param();
  }

  @Test
  public void testOneParam(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    dao.proc_simpletype_param(10);
  }

  @Test
  public void testOneParam_time(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    dao.proc_simpletype_time_param(Time.valueOf("12:34:56"));
  }

  @Test
  public void testIn_InOut_Out(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    Integer param1 = 10;
    Reference<Integer> param2 = new Reference<>(20);
    Reference<Integer> param3 = new Reference<>();
    dao.proc_dto_param(param1, param2, param3);
    assertEquals(Integer.valueOf(10), param1);
    assertEquals(Integer.valueOf(30), param2.get());
    assertEquals(Integer.valueOf(10), param3.get());
  }

  @Test
  public void testIn_InOut_Out_time(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    Time param1 = Time.valueOf("12:34:56");
    Reference<Time> param2 = new Reference<>(Time.valueOf("01:23:45"));
    Reference<Time> param3 = new Reference<>();
    dao.proc_dto_time_param(param1, param2, param3);
    assertEquals(param1, param1);
    assertEquals(param1, param2.get());
    assertEquals(param1, param3.get());
  }

  @Test
  public void testResultSet(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    dao.proc_resultset(employees, 1);
    assertEquals(13, employees.size());
  }

  @Test
  public void testResultSet_check(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    try {
      dao.proc_resultset_check(employees, 1);
      fail();
    } catch (ResultMappingException ignored) {
      System.err.println(ignored);
    }
  }

  @Test
  public void testResultSet_nocheck(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    dao.proc_resultset_nocheck(employees, 1);
    assertEquals(13, employees.size());
  }

  @Test
  public void testResultSet_map(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Map<String, Object>> employees = new ArrayList<>();
    dao.proc_resultset_map(employees, 1);
    assertEquals(13, employees.size());
  }

  @Test
  public void testResultSet_Out(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    Reference<Integer> count = new Reference<>();
    dao.proc_resultset_out(employees, 1, count);
    assertEquals(13, employees.size());
    assertEquals(Integer.valueOf(14), count.get());
  }

  @Test
  public void testResultSetAndUpdate(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    dao.proc_resultset_update(employees, 1);
    assertEquals(13, employees.size());
    DepartmentDao departmentDao = new DepartmentDaoImpl(config);
    Department department = departmentDao.selectById(1);
    assertEquals("HOGE", department.getDepartmentName());
  }

  @Test
  public void testResultSetAndUpdate2(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    dao.proc_resultset_update2(employees, 1);
    assertEquals(13, employees.size());
    DepartmentDao departmentDao = new DepartmentDaoImpl(config);
    Department department = departmentDao.selectById(1);
    assertEquals("HOGE", department.getDepartmentName());
  }

  @Test
  public void testResultSets(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    List<Department> departments = new ArrayList<>();
    dao.proc_resultsets(employees, departments, 1, 1);
    assertEquals(13, employees.size());
    assertEquals(3, departments.size());
  }

  @Test
  public void testResultSetAndUpdate_Out(Config config) throws Exception {
    ProcedureDao dao = new ProcedureDaoImpl(config);
    List<Employee> employees = new ArrayList<>();
    List<Department> departments = new ArrayList<>();
    Reference<Integer> count = new Reference<>();
    dao.proc_resultsets_updates_out(employees, departments, 1, 1, count);
    assertEquals(13, employees.size());
    assertEquals(3, departments.size());
    assertEquals(Integer.valueOf(14), count.get());
  }
}
