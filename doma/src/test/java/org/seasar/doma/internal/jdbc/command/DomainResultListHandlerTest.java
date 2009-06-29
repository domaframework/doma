package org.seasar.doma.internal.jdbc.command;

import java.util.List;

import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.DomainResultListHandler;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.SqlFiles;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainResultListHandlerTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testHandle() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("x"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("aaa"));
        resultSet.rows.add(new RowData("bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFiles
                .buildPath(getClass().getName(), getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        DomainResultListHandler<StringDomain> handler = new DomainResultListHandler<StringDomain>(
                StringDomain.class);
        List<StringDomain> domains = handler.handle(resultSet, query);
        assertEquals(2, domains.size());
        assertEquals(new StringDomain("aaa"), domains.get(0));
        assertEquals(new StringDomain("bbb"), domains.get(1));
    }
}
