package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class Mssql2008ForUpdateTransformerTest extends TestCase {

    public void testForUpdateNormal() throws Exception {
        String expected = "select * from emp with (updlock, rowlock) order by emp.id";
        Mssql2008ForUpdateTransformer transformer = new Mssql2008ForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateNormal_originalSqlNodeUnchanged() throws Exception {
        String original = "select * from emp order by emp.id";
        Mssql2008ForUpdateTransformer transformer = new Mssql2008ForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0);
        SqlParser parser = new SqlParser(original);
        SqlNode originalSqlNode = parser.parse();
        SqlNode sqlNode = transformer.transform(originalSqlNode);
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        sqlBuilder.build(sqlNode, Function.identity());
        sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(originalSqlNode, Function.identity());
        assertEquals(original, sql.getRawSql());
    }

    public void testForUpdateNowait() throws Exception {
        String expected = "select * from emp with (updlock, rowlock, nowait) order by emp.id";
        Mssql2008ForUpdateTransformer transformer = new Mssql2008ForUpdateTransformer(
                SelectForUpdateType.NOWAIT, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }
}
