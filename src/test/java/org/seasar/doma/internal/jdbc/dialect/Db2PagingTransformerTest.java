package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class Db2PagingTransformerTest extends TestCase {

    public void testOffsetLimit() throws Exception {
        String expected = "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
        Db2PagingTransformer transformer = new Db2PagingTransformer(5, 10);
        SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetOnly() throws Exception {
        String expected = "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5";
        Db2PagingTransformer transformer = new Db2PagingTransformer(5, -1);
        SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testLimitOnly() throws Exception {
        String expected = "select emp.id from emp order by emp.id fetch first 10 rows only";
        Db2PagingTransformer transformer = new Db2PagingTransformer(-1, 10);
        SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }
}
