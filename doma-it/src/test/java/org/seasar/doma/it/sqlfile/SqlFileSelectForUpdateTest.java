package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.MessageCode;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class SqlFileSelectForUpdateTest {

    @Prerequisite("#ENV not in {'postgres', 'oracle', 'mysql'}")
    public void testUnsupported() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        try {
            dao.selectById(new IdDomain(1), SelectOptions.get().forUpdate());
            fail();
        } catch (JdbcException expected) {
            assertEquals(MessageCode.DOMA2023, expected.getMessageCode());
        }
    }

    @Prerequisite("#ENV not in {'hsqldb', 'mysql'}")
    public void testForUpdate() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdate());
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdate("employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'oracle', 'mysql'}")
    public void testForUpdateWithTables() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdate("employee"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateNowait() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdateNowait());
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateNowaitWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdateNowait("employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWait() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdateWait(10));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWaitWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = dao.selectById(new IdDomain(1), SelectOptions.get()
                .forUpdateWait(10, "employee_name", "address_id"));
        assertNotNull(employee);
    }
}
