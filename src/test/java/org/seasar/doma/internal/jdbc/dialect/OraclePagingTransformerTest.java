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
public class OraclePagingTransformerTest extends TestCase {

    public void testOffsetLimit() throws Exception {
        String expected = "select * from ( select temp_.*, rownum doma_rownumber_ from ( select * from emp order by emp.id ) temp_ ) where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
        OraclePagingTransformer transformer = new OraclePagingTransformer(5, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetLimit_forUpdate() throws Exception {
        String expected = "select * from ( select temp_.*, rownum doma_rownumber_ from ( select * from emp order by emp.id  ) temp_ ) where doma_rownumber_ > 5 and doma_rownumber_ <= 15 for update";
        OraclePagingTransformer transformer = new OraclePagingTransformer(5, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id for update");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testOffsetOnly() throws Exception {
        String expected = "select * from ( select temp_.*, rownum doma_rownumber_ from ( select * from emp order by emp.id ) temp_ ) where doma_rownumber_ > 5";
        OraclePagingTransformer transformer = new OraclePagingTransformer(5, -1);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }

    public void testLimitOnly() throws Exception {
        String expected = "select * from ( select temp_.*, rownum doma_rownumber_ from ( select * from emp order by emp.id ) temp_ ) where doma_rownumber_ <= 10";
        OraclePagingTransformer transformer = new OraclePagingTransformer(-1, 10);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(),
                SqlKind.SELECT, "dummyPath");
        PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
        assertEquals(expected, sql.getRawSql());
    }
}
