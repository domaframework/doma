package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilderTest extends TestCase {

    private SqlLogFormattingVisitor sqlLogFormattingVisitor = new SqlLogFormattingVisitor();

    public void testAppend() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(
                sqlLogFormattingVisitor);
        builder.appendSql("select * from aaa where name = ");
        builder.appendDomain(new StringDomain("hoge"));
        builder.appendSql(" and salary = ");
        builder.appendDomain(new BigDecimalDomain(new BigDecimal(100)));
        PreparedSql sql = builder.build();
        assertEquals("select * from aaa where name = ? and salary = ?", sql
                .toString());
    }

    public void testCutBackSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(
                sqlLogFormattingVisitor);
        builder.appendSql("select * from aaa where name = ");
        builder.cutBackSql(14);
        PreparedSql sql = builder.build();
        assertEquals("select * from aaa", sql.toString());
    }
}
