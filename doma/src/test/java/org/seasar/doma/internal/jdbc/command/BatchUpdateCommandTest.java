package org.seasar.doma.internal.jdbc.command;

import java.util.Arrays;

import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.internal.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.OptimisticLockException;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class BatchUpdateCommandTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(1);
        emp1.name().set("hoge");
        emp1.version().set(10);

        Emp emp2 = new Emp_();
        emp2.id().set(2);
        emp2.name().set("foo");
        emp2.version().set(20);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        int[] rows = new BatchUpdateCommand(query).execute();

        assertEquals(2, rows.length);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("update emp set name = ?, salary = ?, version = ? + 1 where id = ? and version = ?", sql);
        assertEquals(new IntegerDomain(11), emp1.version());
        assertEquals(new IntegerDomain(21), emp2.version());
    }

    public void testExecute_throwsOptimisticLockException() throws Exception {
        Emp emp = new Emp_();
        emp.id().set(1);
        emp.name().set("hoge");
        emp.version().set(10);

        runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        BatchUpdateCommand command = new BatchUpdateCommand(query);
        try {
            command.execute();
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testExecute_suppressesOptimisticLockException()
            throws Exception {
        Emp emp = new Emp_();
        emp.id().set(1);
        emp.name().set("hoge");
        emp.version().set(10);

        runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp));
        query.setOptimisticLockExceptionSuppressed(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        new BatchUpdateCommand(query).execute();
    }
}
