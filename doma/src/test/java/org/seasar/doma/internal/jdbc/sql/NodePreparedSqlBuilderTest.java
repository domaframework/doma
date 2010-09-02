/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNode;
import org.seasar.doma.internal.jdbc.sql.node.OtherNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WhitespaceNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class NodePreparedSqlBuilderTest extends TestCase {

    private final SqlLocation location = new SqlLocation("dummy sql", 0, 0);

    private final MockConfig config = new MockConfig();

    public void testBindVariableNode() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(WhitespaceNode.of(" "));
        where.addNode(new WordNode("bbb"));
        where.addNode(OtherNode.of(" = "));
        BindVariableNode variable1 = new BindVariableNode(location, "name",
                "/*#name*/");
        variable1.setWordNode(new WordNode("'hoge'"));
        where.addNode(variable1);
        where.addNode(WhitespaceNode.of(" "));
        where.addNode(new LogicalOperatorNode("and"));
        where.addNode(WhitespaceNode.of(" "));
        where.addNode(new WordNode("ccc"));
        where.addNode(OtherNode.of(" = "));
        BindVariableNode variable2 = new BindVariableNode(location, "salary",
                "/*#salary*/");
        variable2.setWordNode(new WordNode("100"));
        where.addNode(variable2);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary",
                new Value(BigDecimal.class, new BigDecimal(100)));
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(root);
        assertEquals("select * from aaa where bbb = ? and ccc = ?",
                sql.getRawSql());
        assertEquals(2, sql.getParameters().size());
        assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
        assertEquals(new BigDecimal(100), sql.getParameters().get(1)
                .getWrapper().get());
    }

    public void testIfNode_true() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(WhitespaceNode.of(" "));
        IfNode ifNode = new IfNode(location, "true", "/*if true*/");
        ifNode.addNode(new WordNode("bbb"));
        ifNode.addNode(OtherNode.of(" = "));
        ifNode.addNode(new WordNode("ccc"));
        IfBlockNode ifBlockNode = new IfBlockNode();
        ifBlockNode.setIfNode(ifNode);
        ifBlockNode.setEndNode(new EndNode("/*end*/"));
        where.addNode(ifBlockNode);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(statement);
        assertEquals("select * from aaa where bbb = ccc", sql.getRawSql());
    }

    public void testIfNode_false() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(WhitespaceNode.of(" "));
        IfNode ifNode = new IfNode(location, "false", "/*if false*/");
        ifNode.addNode(new WordNode("bbb"));
        ifNode.addNode(OtherNode.of(" = "));
        ifNode.addNode(new WordNode("ccc"));
        IfBlockNode ifBlockNode = new IfBlockNode();
        ifBlockNode.setIfNode(ifNode);
        ifBlockNode.setEndNode(new EndNode("/*end*/"));
        where.addNode(ifBlockNode);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(statement);
        assertEquals("select * from aaa", sql.getRawSql());
    }

    public void testElseNode() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(WhitespaceNode.of(" "));
        IfNode ifNode = new IfNode(location, "false", "/*if false*/");
        ifNode.addNode(new WordNode("bbb"));
        ifNode.addNode(OtherNode.of(" = "));
        ifNode.addNode(new WordNode("ccc"));
        ElseNode elseNode = new ElseNode("/*else*/");
        elseNode.addNode(new WordNode("ddd"));
        elseNode.addNode(OtherNode.of(" = "));
        elseNode.addNode(new WordNode("eee"));
        EndNode endNode = new EndNode("/*end*/");
        IfBlockNode ifBlock = new IfBlockNode();
        ifBlock.setIfNode(ifNode);
        ifBlock.setElseNode(elseNode);
        ifBlock.setEndNode(endNode);
        where.addNode(ifBlock);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(statement);
        assertEquals("select * from aaa where ddd = eee", sql.getRawSql());
    }

    public void testWhere() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa", sql.getRawSql());
    }

    public void testWhere_embeddedVariable() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("embedded", new Value(String.class, "bbb = ccc"));
        String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa where  bbb = ccc", sql.getRawSql());
    }

    public void testWhere_embeddedVariable_orderBy() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("embedded", new Value(String.class, "order by bbb"));
        String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa   order by bbb", sql.getRawSql());
    }

    public void testWhere_embeddedVariable_orderBy_followedByForUpdate()
            throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("embedded", new Value(String.class, "order by bbb"));
        String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/ for update";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        PreparedSql sql = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                "dummyPath", evaluator).build(sqlNode);
        assertEquals("select * from aaa   order by bbb for update",
                sql.getRawSql());
    }

    public void testAndNode() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        IfNode ifNode1 = new IfNode(location, "true", "/*if true*/");
        ifNode1.addNode(WhitespaceNode.of(" "));
        ifNode1.addNode(new WordNode("bbb"));
        ifNode1.addNode(OtherNode.of(" = "));
        ifNode1.addNode(new WordNode("ccc"));
        EndNode endNode1 = new EndNode("/*end*/");
        IfBlockNode ifBlock1 = new IfBlockNode();
        ifBlock1.setIfNode(ifNode1);
        ifBlock1.setEndNode(endNode1);
        where.addNode(ifBlock1);
        IfNode ifNode2 = new IfNode(location, "true", "/*if true*/");
        ifNode2.addNode(WhitespaceNode.of(" "));
        LogicalOperatorNode and = new LogicalOperatorNode("and");
        ifNode2.addNode(and);
        and.addNode(WhitespaceNode.of(" "));
        and.addNode(new WordNode("ddd"));
        and.addNode(OtherNode.of(" = "));
        and.addNode(new WordNode("eee"));
        EndNode endNode2 = new EndNode("/*end*/");
        IfBlockNode ifBlock2 = new IfBlockNode();
        ifBlock2.setIfNode(ifNode2);
        ifBlock2.setEndNode(endNode2);
        where.addNode(ifBlock2);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(statement);
        assertEquals("select * from aaa where bbb = ccc and ddd = eee",
                sql.getRawSql());
    }

    public void testAndNode_remove() throws Exception {
        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(OtherNode.of(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(WhitespaceNode.of(" "));
        from.addNode(new WordNode("aaa"));
        from.addNode(WhitespaceNode.of(" "));
        WhereClauseNode where = new WhereClauseNode("where");
        IfNode ifNode1 = new IfNode(location, "false", "/*if false*/");
        ifNode1.addNode(WhitespaceNode.of(" "));
        ifNode1.addNode(new WordNode("bbb"));
        ifNode1.addNode(OtherNode.of(" = "));
        ifNode1.addNode(new WordNode("ccc"));
        EndNode endNode1 = new EndNode("/*end*/");
        IfBlockNode ifBlock1 = new IfBlockNode();
        ifBlock1.setIfNode(ifNode1);
        ifBlock1.setEndNode(endNode1);
        where.addNode(ifBlock1);
        IfNode ifNode2 = new IfNode(location, "true", "/*if true*/");
        ifNode2.addNode(WhitespaceNode.of(" "));
        LogicalOperatorNode and = new LogicalOperatorNode("and");
        ifNode2.addNode(and);
        and.addNode(WhitespaceNode.of(" "));
        and.addNode(new WordNode("ddd"));
        and.addNode(OtherNode.of(" = "));
        and.addNode(new WordNode("eee"));
        EndNode endNode2 = new EndNode("/*end*/");
        IfBlockNode ifBlock2 = new IfBlockNode();
        ifBlock2.setIfNode(ifNode2);
        ifBlock2.setEndNode(endNode2);
        where.addNode(ifBlock2);

        SelectStatementNode statement = new SelectStatementNode();
        statement.setSelectClauseNode(select);
        statement.setFromClauseNode(from);
        statement.setWhereClauseNode(where);
        AnonymousNode root = new AnonymousNode();
        root.addNode(statement);

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        NodePreparedSqlBuilder builder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, "dummyPath", evaluator);
        PreparedSql sql = builder.build(statement);
        assertEquals("select * from aaa where  ddd = eee", sql.getRawSql());
    }

    public void testEmbeddedVariable_containsSingleQuote() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("orderBy", new Value(String.class, "aaa'"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        try {
            new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath",
                    evaluator).build(sqlNode);
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2116, expected.getMessageResource());
        }
    }

    public void testEmbeddedVariable_containsSemicolon() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("orderBy", new Value(String.class, "aaa;bbb"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        try {
            new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath",
                    evaluator).build(sqlNode);
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2117, expected.getMessageResource());
        }
    }

    public void testEmbeddedVariable_lineComment() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("orderBy", new Value(String.class, "aaa--bbb"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        try {
            new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath",
                    evaluator).build(sqlNode);
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2122, expected.getMessageResource());
        }
    }

    public void testEmbeddedVariable_blockComment() throws Exception {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("name", new Value(String.class, "hoge"));
        evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(
                10000)));
        evaluator.add("orderBy", new Value(String.class, "aaa/*bbb"));
        String testSql = "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
        SqlParser parser = new SqlParser(testSql);
        SqlNode sqlNode = parser.parse();
        try {
            new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath",
                    evaluator).build(sqlNode);
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected.getMessage());
            assertEquals(Message.DOMA2123, expected.getMessageResource());
        }
    }

}
