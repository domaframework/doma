package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.dialect.StandardPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class StandardPagingTransformerTest extends TestCase {

    public void testOffsetLimit() throws Exception {
        String expected = "select * from ( select temp_.*, row_number() over( order by temp_.id ) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_ where rownumber_ > 5 and rownumber_ <= 15";
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                5, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetOnly() throws Exception {
        String expected = "select * from ( select temp_.*, row_number() over( order by temp_.id ) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_ where rownumber_ > 5";
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                5, -1);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testLimitOnly() throws Exception {
        String expected = "select * from ( select temp_.*, row_number() over( order by temp_.id ) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_ where rownumber_ <= 10";
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                -1, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

    public void testOrderByClauseUnspecified() throws Exception {
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                5, 10);
        SqlParser parser = new SqlParser("select * from emp");
        try {
            transformer.transform(parser.parse());
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(MessageCode.DOMA2201, expected.getMessageCode());
        }
    }
}
