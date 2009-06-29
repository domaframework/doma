package org.seasar.doma.internal.jdbc;

import java.util.Arrays;

import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.StandardSqlFileRepository;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class JdbcExceptionTest extends TestCase {

    public void testSqlFileNotFound() throws Exception {
        StandardSqlFileRepository repository = new StandardSqlFileRepository();
        try {
            repository.getSqlFile("aaa/bbb.sql", new StandardDialect());
            fail();
        } catch (SqlFileNotFoundException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2011, e.getMessageCode());
        }
    }

    public void testQuotationNotClosed() throws Exception {
        SqlParser parser = new SqlParser("select * from 'aaa");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2101, e.getMessageCode());
        }
    }

    public void testBlockCommentNotClosed() throws Exception {
        SqlParser parser = new SqlParser("select * from aaa /*aaa");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2102, e.getMessageCode());
        }
    }

    public void testElseifCommentNotClosed() throws Exception {
        SqlParser parser = new SqlParser("select * from aaa --elseif bbb");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2103, e.getMessageCode());
        }
    }

    public void testIfCommentNotFoundForEndComment() throws Exception {
        SqlParser parser = new SqlParser("select * from aaa/*%end*/ ");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2104, e.getMessageCode());
        }
    }

    public void testIfCommentNotFoundForSecondEndComment() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where/*%if true*//*%end*/ /*%end*/");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2104, e.getMessageCode());
        }
    }

    public void testIfCommentNotFoundForElseComment() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb = ccc--else ddd = eee");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2105, e.getMessageCode());
        }
    }

    public void testIfCommentNotFoundForElseifComment() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb = ccc--elseif true--ddd = eee");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2106, e.getMessageCode());
        }
    }

    public void testElseCommentDuplicated() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where /*%if true*/bbb = ccc--else --else ddd = eee");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2107, e.getMessageCode());
        }
    }

    public void testElseifCommentFollowsElseComment() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where /*%if true*/bbb = ccc--else ddd = eee --elseif ture--");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2108, e.getMessageCode());
        }
    }

    public void testOpenedParensNotFound() throws Exception {
        SqlParser parser = new SqlParser("select * from aaa where )");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2109, e.getMessageCode());
        }
    }

    public void testTestLiteralNotFound() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb = /*bbb*/ 'ccc')");
        try {
            parser.parse();
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2110, e.getMessageCode());
        }
    }

    public void testSqlBuildingFailed() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb = \n/*bbb*/'ccc'");
        SqlNode sqlNode = parser.parse();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(
                new SqlLogFormattingVisitor());
        try {
            builder.build(sqlNode);
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2111, e.getMessageCode());
        }
    }

    public void testBindValueTypeNotCollection() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb in /*bbb*/(1,2,3)");
        SqlNode sqlNode = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("bbb", new IntegerDomain(1));
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(evaluator,
                new SqlLogFormattingVisitor());
        try {
            builder.build(sqlNode);
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2112, e.getMessageCode());
        }
    }

    public void testCollectionElementOfBindValueTypeNotDomain()
            throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb in /*bbb*/(1,2,3)");
        SqlNode sqlNode = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("bbb", Arrays.asList(1));
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(evaluator,
                new SqlLogFormattingVisitor());
        try {
            builder.build(sqlNode);
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2113, e.getMessageCode());
        }
    }

    public void testBindValueTypeNotDomain() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb = /*bbb*/1");
        SqlNode sqlNode = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("bbb", 1);
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(evaluator,
                new SqlLogFormattingVisitor());
        try {
            builder.build(sqlNode);
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2114, e.getMessageCode());
        }
    }

    public void testCollectionOfBindValueContainsNull() throws Exception {
        SqlParser parser = new SqlParser(
                "select * from aaa where bbb in /*bbb*/(1,2,3)");
        SqlNode sqlNode = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("bbb", Arrays.asList(new IntegerDomain(1), null));
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(evaluator,
                new SqlLogFormattingVisitor());
        try {
            builder.build(sqlNode);
            fail();
        } catch (JdbcException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA2115, e.getMessageCode());
        }
    }
}
