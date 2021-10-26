package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileSelectForUpdateTest {

  @Test
  @Run(unless = {Dbms.H2, Dbms.POSTGRESQL, Dbms.ORACLE, Dbms.MYSQL, Dbms.DB2, Dbms.SQLSERVER})
  public void testUnsupported(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    try {
      dao.selectById(1, SelectOptions.get().forUpdate());
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2023, expected.getMessageResource());
    }
  }

  @Test
  @Run(unless = {Dbms.HSQLDB, Dbms.SQLITE})
  public void testForUpdate(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectById(1, SelectOptions.get().forUpdate());
    assertNotNull(employee);
  }

  @Test
  @Run(
      unless = {
        Dbms.HSQLDB,
        Dbms.H2,
        Dbms.POSTGRESQL,
        Dbms.MYSQL,
        Dbms.DB2,
        Dbms.SQLSERVER,
        Dbms.SQLITE
      })
  public void testForUpdateWithColumns(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee =
        dao.selectById(1, SelectOptions.get().forUpdate("employee_name", "address_id"));
    assertNotNull(employee);
  }

  @Test
  @Run(
      unless = {
        Dbms.HSQLDB,
        Dbms.H2,
        Dbms.ORACLE,
        Dbms.MYSQL,
        Dbms.DB2,
        Dbms.SQLSERVER,
        Dbms.SQLITE
      })
  public void testForUpdateWithTables(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectById(1, SelectOptions.get().forUpdate("employee"));
    assertNotNull(employee);
  }

  @Test
  @Run(unless = {Dbms.HSQLDB, Dbms.H2, Dbms.POSTGRESQL, Dbms.MYSQL, Dbms.DB2, Dbms.SQLITE})
  public void testForUpdateNowait(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectById(1, SelectOptions.get().forUpdateNowait());
    assertNotNull(employee);
  }

  @Test
  @Run(
      unless = {
        Dbms.HSQLDB,
        Dbms.H2,
        Dbms.POSTGRESQL,
        Dbms.MYSQL,
        Dbms.DB2,
        Dbms.SQLSERVER,
        Dbms.SQLITE
      })
  public void testForUpdateNowaitWithColumns(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee =
        dao.selectById(1, SelectOptions.get().forUpdateNowait("employee_name", "address_id"));
    assertNotNull(employee);
  }

  @Test
  @Run(
      unless = {
        Dbms.HSQLDB,
        Dbms.H2,
        Dbms.POSTGRESQL,
        Dbms.MYSQL,
        Dbms.DB2,
        Dbms.SQLSERVER,
        Dbms.SQLITE
      })
  public void testForUpdateWait(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectById(1, SelectOptions.get().forUpdateWait(10));
    assertNotNull(employee);
  }

  @Test
  @Run(
      unless = {
        Dbms.HSQLDB,
        Dbms.H2,
        Dbms.POSTGRESQL,
        Dbms.MYSQL,
        Dbms.DB2,
        Dbms.SQLSERVER,
        Dbms.SQLITE
      })
  public void testForUpdateWaitWithColumns(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee =
        dao.selectById(1, SelectOptions.get().forUpdateWait(10, "employee_name", "address_id"));
    assertNotNull(employee);
  }
}
