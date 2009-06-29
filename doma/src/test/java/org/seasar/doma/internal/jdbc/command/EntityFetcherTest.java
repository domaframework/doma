package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;

import org.seasar.doma.internal.jdbc.command.EntityFetcher;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;

import junit.framework.TestCase;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class EntityFetcherTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testFetch() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("id"));
        metaData.columns.add(new ColumnMetaData("name"));
        metaData.columns.add(new ColumnMetaData("salary"));
        metaData.columns.add(new ColumnMetaData("version"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData(1, "aaa", new BigDecimal(10), 100));
        resultSet.next();

        Emp_ emp = new Emp_();
        EntityFetcher fetcher = new EntityFetcher(new MySelectQuery());
        fetcher.fetch(resultSet, emp);

        assertEquals(new Integer(1), emp.id().get());
        assertEquals("aaa", emp.name().get());
        assertEquals(new BigDecimal(10), emp.salary().get());
        assertEquals(new Integer(100), emp.version().get());
    }

    protected class MySelectQuery implements SelectQuery {

        @Override
        public SelectOptions getOptions() {
            return SelectOptions.get();
        }

        @Override
        public Config getConfig() {
            return runtimeConfig;
        }

        @Override
        public String getClassName() {
            return null;
        }

        @Override
        public String getMethodName() {
            return null;
        }

        @Override
        public PreparedSql getSql() {
            return null;
        }

        @Override
        public int getFetchSize() {
            return 0;
        }

        @Override
        public int getMaxRows() {
            return 0;
        }

        @Override
        public int getQueryTimeout() {
            return 0;
        }

    }
}
