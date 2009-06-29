package org.seasar.doma.internal.jdbc.command;

import java.util.Arrays;

import org.seasar.doma.internal.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchDeleteQuery;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class BatchDeleteCommandTest extends TestCase {

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

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        int[] rows = new BatchDeleteCommand(query).execute();

        assertEquals(2, rows.length);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("delete from emp where id = ? and version = ?", sql);
    }

}
