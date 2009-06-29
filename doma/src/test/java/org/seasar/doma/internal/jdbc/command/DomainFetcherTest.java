package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;

import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.DomainFetcher;
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

/**
 * @author taedium
 * 
 */
public class DomainFetcherTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testFetchDomain() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("aaa"));
        metaData.columns.add(new ColumnMetaData("bbb"));
        metaData.columns.add(new ColumnMetaData("ccc"));
        metaData.columns.add(new ColumnMetaData("ddd"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("hoge", "foo", new BigDecimal(10), 100));
        resultSet.next();

        StringDomain aaa = new StringDomain();
        DomainFetcher fetcher = new DomainFetcher(new MySelectQuery());
        fetcher.fetch(resultSet, aaa);

        assertEquals("hoge", aaa.get());
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
