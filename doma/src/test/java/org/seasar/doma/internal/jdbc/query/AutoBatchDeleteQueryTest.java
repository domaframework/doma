package org.seasar.doma.internal.jdbc.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.internal.jdbc.query.BatchDeleteQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.name().set("bbb");

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchDeleteQuery batchDeleteQuery = query;
        assertEquals(2, batchDeleteQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(new Integer(10));

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from emp where id = ? and version = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new IntegerDomain(10), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());

        sql = query.getSqls().get(1);
        assertEquals("delete from emp where id = ? and version = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new IntegerDomain(20), parameters.get(0).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(1).getDomain());
    }

    public void testOption_ignoresVersion() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(new Integer(10));

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setVersionIgnored(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from emp where id = ?", sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new IntegerDomain(10), parameters.get(0).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("delete from emp where id = ?", sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new IntegerDomain(20), parameters.get(0).getDomain());
    }
}
