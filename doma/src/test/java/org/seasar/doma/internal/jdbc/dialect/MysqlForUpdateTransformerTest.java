package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.dialect.MysqlForUpdateTransformer;
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
public class MysqlForUpdateTransformerTest extends TestCase {

    public void testForUpdateNormal() throws Exception {
        String expected = "select * from emp order by emp.id for update";
        MysqlForUpdateTransformer transformer = new MysqlForUpdateTransformer(
                SelectForUpdateType.NORMAL, 0);
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = transformer.transform(parser.parse());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        PreparedSql sql = sqlBuilder.build(sqlNode);
        assertEquals(expected, sql.getRawSql());
    }

}
