package org.seasar.doma.internal.jdbc.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.internal.jdbc.query.BatchInsertQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class AutoBatchInsertQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.name().set("bbb");

        AutoBatchInsertQuery<Emp, Emp_> query = new AutoBatchInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.compile();

        BatchInsertQuery batchInsertQuery = query;
        assertTrue(batchInsertQuery.isBatchSupported());
        assertEquals(2, batchInsertQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(new Integer(10));

        AutoBatchInsertQuery<Emp, Emp_> query = new AutoBatchInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("insert into emp (id, name, salary, version) values (?, ?, ?, ?)", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals(new IntegerDomain(10), parameters.get(0).getDomain());
        assertEquals(new StringDomain("aaa"), parameters.get(1).getDomain());
        assertTrue(parameters.get(2).getDomain().isNull());
        assertEquals(new IntegerDomain(1), parameters.get(3).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("insert into emp (id, name, salary, version) values (?, ?, ?, ?)", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals(new IntegerDomain(20), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new BigDecimalDomain(new BigDecimal(2000)), parameters
                .get(2).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(3).getDomain());
    }
}
