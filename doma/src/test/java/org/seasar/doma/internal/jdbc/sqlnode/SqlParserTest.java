package org.seasar.doma.internal.jdbc.sqlnode;

import java.math.BigDecimal;
import java.util.Arrays;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
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
public class SqlParserTest extends TestCase {

    private SqlLogFormattingVisitor sqlLogFormattingVisitor = new SqlLogFormattingVisitor();

    public void testBindVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain("hoge"));
        evaluator.add("salary", new BigDecimalDomain(new BigDecimal(10000)));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/2000";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where ename = ? and sal = ?", sql
                .getRawSql());
        assertEquals("select * from aaa where ename = 'hoge' and sal = 10000", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(0)
                .getDomain());
        assertEquals(new BigDecimalDomain(new BigDecimal(10000)), sql
                .getParameters().get(1).getDomain());
    }

    public void testBindVariable_in() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", Arrays
                .asList(new StringDomain("hoge"), new StringDomain("foo")));
        String testSql = "select * from aaa where ename in /*name*/('aaa', 'bbb')";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where ename in (?, ?)", sql.getRawSql());
        assertEquals("select * from aaa where ename in ('hoge', 'foo')", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(0)
                .getDomain());
        assertEquals(new StringDomain("foo"), sql.getParameters().get(1)
                .getDomain());
    }

    public void testIf() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge'", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(0)
                .getDomain());
    }

    public void testIf_removeWhere() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain());
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
        assertEquals("select * from aaa", sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(0, sql.getParameters().size());
    }

    public void testIf_nest() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name != null*/bbb = /*name*/'ccc' /*%if name == \"hoge\"*/and ddd = eee/*%end*//*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where bbb = ? and ddd = eee", sql
                .getRawSql());
        assertEquals("select * from aaa where bbb = 'hoge' and ddd = eee", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
    }

    public void testElseif() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain(""));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name ==\"\"--bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where bbb = ''", sql.getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new StringDomain(""), sql.getParameters().get(0)
                .getDomain());
    }

    public void testElse() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain("hoge"));
        String testSql = "select * from aaa where /*%if name == null*/bbb is null--elseif name == \"\"----else bbb = /*name*/'ccc'/*%end*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select * from aaa where  bbb = ?", sql.getRawSql());
        assertEquals("select * from aaa where  bbb = 'hoge'", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(1, sql.getParameters().size());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(0)
                .getDomain());
    }

    public void testSelect() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new StringDomain("hoge"));
        evaluator.add("count", new IntegerDomain(5));
        String testSql = "select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = /*name*/'ccc' group by aaa.deptname having count(*) > /*count*/10 order by aaa.name for update bbb";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = ? group by aaa.deptname having count(*) > ? order by aaa.name for update bbb", sql
                .getRawSql());
        assertEquals("select aaa.deptname, count(*) from aaa join bbb on aaa.id = bbb.id where aaa.name = 'hoge' group by aaa.deptname having count(*) > 5 order by aaa.name for update bbb", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(2, sql.getParameters().size());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(0)
                .getDomain());
        assertEquals(new IntegerDomain(5), sql.getParameters().get(1)
                .getDomain());
    }

    public void testUpdate() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("no", new IntegerDomain(10));
        evaluator.add("name", new StringDomain("hoge"));
        evaluator.add("id", new IntegerDomain(100));
        String testSql = "update aaa set no = /*no*/1, set name = /*name*/'name' where id = /*id*/1";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(evaluator,
                sqlLogFormattingVisitor).build(sqlNode);
        assertEquals("update aaa set no = ?, set name = ? where id = ?", sql
                .getRawSql());
        assertEquals("update aaa set no = 10, set name = 'hoge' where id = 100", sql
                .getFormattedSql());
        assertEquals(testSql, sqlNode.toString());
        assertEquals(3, sql.getParameters().size());
        assertEquals(new IntegerDomain(10), sql.getParameters().get(0)
                .getDomain());
        assertEquals(new StringDomain("hoge"), sql.getParameters().get(1)
                .getDomain());
        assertEquals(new IntegerDomain(100), sql.getParameters().get(2)
                .getDomain());
    }
}
