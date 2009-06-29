package org.seasar.doma.internal.jdbc.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.internal.jdbc.query.BatchUpdateQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class AutoBatchUpdateQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.name().set("bbb");
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchUpdateQuery batchUpdateQuery = query;
        assertEquals(2, batchUpdateQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("update emp set name = ?, salary = ?, version = ? + 1 where id = ? and version = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(5, parameters.size());
        assertEquals(new StringDomain("aaa"), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new IntegerDomain(100), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(3).getDomain());
        assertEquals(new IntegerDomain(100), parameters.get(4).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("update emp set name = ?, salary = ?, version = ? + 1 where id = ? and version = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(5, parameters.size());
        assertTrue(parameters.get(0).getDomain().isNull());
        assertEquals(new BigDecimalDomain(new BigDecimal(2000)), parameters
                .get(1).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(20), parameters.get(3).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(4).getDomain());
    }

    public void testOption_includesVersion() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setVersionIncluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("update emp set name = ?, salary = ?, version = ? where id = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals(new StringDomain("aaa"), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new IntegerDomain(100), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(3).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("update emp set name = ?, salary = ?, version = ? where id = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertTrue(parameters.get(0).getDomain().isNull());
        assertEquals(new BigDecimalDomain(new BigDecimal(2000)), parameters
                .get(1).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(20), parameters.get(3).getDomain());
    }

    public void testIsExecutable() throws Exception {
        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Collections.<Emp> emptyList());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        assertFalse(query.isExecutable());
    }

    public void testIllegalEntityInstance() throws Exception {
        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        try {
            query.setEntities(Arrays.<Emp> asList(new MyEmp()));
            fail();
        } catch (JdbcException expected) {
            assertEquals(MessageCode.DOMA2026, expected.getMessageCode());
        }
    }

    private static class MyEmp implements Emp {

        @Override
        public IntegerDomain version() {
            return null;
        }

        @Override
        public BigDecimalDomain salary() {
            return null;
        }

        @Override
        public StringDomain name() {
            return null;
        }

        @Override
        public IntegerDomain id() {
            return null;
        }
    }
}
