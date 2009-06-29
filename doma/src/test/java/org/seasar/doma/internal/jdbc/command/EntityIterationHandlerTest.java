package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.SqlFiles;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class EntityIterationHandlerTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testHandle() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("id"));
        metaData.columns.add(new ColumnMetaData("name"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData(1, "aaa"));
        resultSet.rows.add(new RowData(2, "bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFiles
                .buildPath(getClass().getName(), getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        EntityIterationHandler<Integer, Emp, Emp_> handler = new EntityIterationHandler<Integer, Emp, Emp_>(
                Emp_.class, new IterationCallback<Integer, Emp>() {

                    private int count;

                    @Override
                    public Integer iterate(Emp target,
                            IterationContext iterationContext) {
                        count++;
                        return count;
                    }

                });
        Integer result = handler.handle(resultSet, query);
        assertEquals(new Integer(2), result);
    }

    public void testHandle_exits() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("id"));
        metaData.columns.add(new ColumnMetaData("name"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData(1, "aaa"));
        resultSet.rows.add(new RowData(2, "bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFiles
                .buildPath(getClass().getName(), getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        EntityIterationHandler<Integer, Emp, Emp_> handler = new EntityIterationHandler<Integer, Emp, Emp_>(
                Emp_.class, new IterationCallback<Integer, Emp>() {

                    private int count;

                    @Override
                    public Integer iterate(Emp target,
                            IterationContext iterationContext) {
                        iterationContext.exits();
                        count++;
                        return count;
                    }

                });
        Integer result = handler.handle(resultSet, query);
        assertEquals(new Integer(1), result);
    }
}
