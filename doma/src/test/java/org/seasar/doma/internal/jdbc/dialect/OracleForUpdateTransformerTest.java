package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.dialect.OracleForUpdateTransformer;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class OracleForUpdateTransformerTest extends TestCase {

    public void testForUpdateNormal() throws Exception {
        String expected = "select * from emp order by emp.id for update";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateNormal_alias() throws Exception {
        String expected = "select * from emp order by emp.id for update of emp.name, emp.salary";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0, "emp.name", "emp.salary");
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateNowait() throws Exception {
        String expected = "select * from emp order by emp.id for update nowait";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.NOWAIT, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testForUpdateWait() throws Exception {
        String expected = "select * from emp order by emp.id for update wait 10";
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                SelectForUpdateType.WAIT, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }
}
