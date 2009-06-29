package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class PostgresPagingTransformerTest extends TestCase {

    public void testOffsetLimit() throws Exception {
        String expected = "select * from emp order by emp.id limit 10 offset 5";
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                5, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetLimit_forUpdate() throws Exception {
        String expected = "select * from emp order by emp.id  limit 10 offset 5 for update";
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                5, 10);
        SqlParser parser = new SqlParser(
                "select * from emp order by emp.id for update");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetOnly() throws Exception {
        String expected = "select * from emp order by emp.id offset 5";
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                5, -1);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testLimitOnly() throws Exception {
        String expected = "select * from emp order by emp.id limit 10";
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                -1, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }
}
