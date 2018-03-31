package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;
import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.node.*;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.message.Message;

/** @author taedium */
public class NodePreparedSqlBuilderTest extends TestCase {

  private final SqlLocation location = new SqlLocation("dummy sql", 0, 0);

  private final MockConfig config = new MockConfig();

  public void testBindVariableNode() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(WhitespaceNode.of(" "));
    where.appendNode(new WordNode("bbb"));
    where.appendNode(OtherNode.of(" = "));
    BindVariableNode variable1 = new BindVariableNode(location, "name", "/*#name*/");
    variable1.setWordNode(new WordNode("'hoge'"));
    where.appendNode(variable1);
    where.appendNode(WhitespaceNode.of(" "));
    where.appendNode(new LogicalOperatorNode("and"));
    where.appendNode(WhitespaceNode.of(" "));
    where.appendNode(new WordNode("ccc"));
    where.appendNode(OtherNode.of(" = "));
    BindVariableNode variable2 = new BindVariableNode(location, "salary", "/*#salary*/");
    variable2.setWordNode(new WordNode("100"));
    where.appendNode(variable2);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(100)));
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(root, Function.identity());
    assertEquals("select * from aaa where bbb = ? and ccc = ?", sql.getRawSql());
    assertEquals(2, sql.getParameters().size());
    assertEquals("hoge", sql.getParameters().get(0).getWrapper().get());
    assertEquals(new BigDecimal(100), sql.getParameters().get(1).getWrapper().get());
  }

  public void testIfNode_true() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(WhitespaceNode.of(" "));
    IfNode ifNode = new IfNode(location, "true", "/*if true*/");
    ifNode.appendNode(new WordNode("bbb"));
    ifNode.appendNode(OtherNode.of(" = "));
    ifNode.appendNode(new WordNode("ccc"));
    IfBlockNode ifBlockNode = new IfBlockNode();
    ifBlockNode.setIfNode(ifNode);
    ifBlockNode.setEndNode(new EndNode("/*end*/"));
    where.appendNode(ifBlockNode);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(statement, Function.identity());
    assertEquals("select * from aaa where bbb = ccc", sql.getRawSql());
  }

  public void testIfNode_false() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(WhitespaceNode.of(" "));
    IfNode ifNode = new IfNode(location, "false", "/*if false*/");
    ifNode.appendNode(new WordNode("bbb"));
    ifNode.appendNode(OtherNode.of(" = "));
    ifNode.appendNode(new WordNode("ccc"));
    IfBlockNode ifBlockNode = new IfBlockNode();
    ifBlockNode.setIfNode(ifNode);
    ifBlockNode.setEndNode(new EndNode("/*end*/"));
    where.appendNode(ifBlockNode);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(statement, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
  }

  public void testElseNode() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(WhitespaceNode.of(" "));
    IfNode ifNode = new IfNode(location, "false", "/*if false*/");
    ifNode.appendNode(new WordNode("bbb"));
    ifNode.appendNode(OtherNode.of(" = "));
    ifNode.appendNode(new WordNode("ccc"));
    ElseNode elseNode = new ElseNode("/*else*/");
    elseNode.appendNode(new WordNode("ddd"));
    elseNode.appendNode(OtherNode.of(" = "));
    elseNode.appendNode(new WordNode("eee"));
    EndNode endNode = new EndNode("/*end*/");
    IfBlockNode ifBlock = new IfBlockNode();
    ifBlock.setIfNode(ifNode);
    ifBlock.setElseNode(elseNode);
    ifBlock.setEndNode(endNode);
    where.appendNode(ifBlock);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(statement, Function.identity());
    assertEquals("select * from aaa where ddd = eee", sql.getRawSql());
  }

  public void testWhere() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa", sql.getRawSql());
  }

  public void testWhere_embeddedVariable() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("embedded", new Value(String.class, "bbb = ccc"));
    String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa where  bbb = ccc", sql.getRawSql());
  }

  public void testWhere_embeddedVariable_orderBy() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("embedded", new Value(String.class, "order by bbb"));
    String testSql = "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa   order by bbb", sql.getRawSql());
  }

  public void testWhere_embeddedVariable_orderBy_followedByForUpdate() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("embedded", new Value(String.class, "order by bbb"));
    String testSql =
        "select * from aaa where /*%if false*/ename = 'aaa'/*%end */ /*#embedded*/ for update";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from aaa   order by bbb for update", sql.getRawSql());
  }

  public void testAndNode() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    IfNode ifNode1 = new IfNode(location, "true", "/*if true*/");
    ifNode1.appendNode(WhitespaceNode.of(" "));
    ifNode1.appendNode(new WordNode("bbb"));
    ifNode1.appendNode(OtherNode.of(" = "));
    ifNode1.appendNode(new WordNode("ccc"));
    EndNode endNode1 = new EndNode("/*end*/");
    IfBlockNode ifBlock1 = new IfBlockNode();
    ifBlock1.setIfNode(ifNode1);
    ifBlock1.setEndNode(endNode1);
    where.appendNode(ifBlock1);
    IfNode ifNode2 = new IfNode(location, "true", "/*if true*/");
    ifNode2.appendNode(WhitespaceNode.of(" "));
    LogicalOperatorNode and = new LogicalOperatorNode("and");
    ifNode2.appendNode(and);
    and.appendNode(WhitespaceNode.of(" "));
    and.appendNode(new WordNode("ddd"));
    and.appendNode(OtherNode.of(" = "));
    and.appendNode(new WordNode("eee"));
    EndNode endNode2 = new EndNode("/*end*/");
    IfBlockNode ifBlock2 = new IfBlockNode();
    ifBlock2.setIfNode(ifNode2);
    ifBlock2.setEndNode(endNode2);
    where.appendNode(ifBlock2);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(statement, Function.identity());
    assertEquals("select * from aaa where bbb = ccc and ddd = eee", sql.getRawSql());
  }

  public void testAndNode_remove() throws Exception {
    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(OtherNode.of(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(WhitespaceNode.of(" "));
    from.appendNode(new WordNode("aaa"));
    from.appendNode(WhitespaceNode.of(" "));
    WhereClauseNode where = new WhereClauseNode("where");
    IfNode ifNode1 = new IfNode(location, "false", "/*if false*/");
    ifNode1.appendNode(WhitespaceNode.of(" "));
    ifNode1.appendNode(new WordNode("bbb"));
    ifNode1.appendNode(OtherNode.of(" = "));
    ifNode1.appendNode(new WordNode("ccc"));
    EndNode endNode1 = new EndNode("/*end*/");
    IfBlockNode ifBlock1 = new IfBlockNode();
    ifBlock1.setIfNode(ifNode1);
    ifBlock1.setEndNode(endNode1);
    where.appendNode(ifBlock1);
    IfNode ifNode2 = new IfNode(location, "true", "/*if true*/");
    ifNode2.appendNode(WhitespaceNode.of(" "));
    LogicalOperatorNode and = new LogicalOperatorNode("and");
    ifNode2.appendNode(and);
    and.appendNode(WhitespaceNode.of(" "));
    and.appendNode(new WordNode("ddd"));
    and.appendNode(OtherNode.of(" = "));
    and.appendNode(new WordNode("eee"));
    EndNode endNode2 = new EndNode("/*end*/");
    IfBlockNode ifBlock2 = new IfBlockNode();
    ifBlock2.setIfNode(ifNode2);
    ifBlock2.setEndNode(endNode2);
    where.appendNode(ifBlock2);

    SelectStatementNode statement = new SelectStatementNode();
    statement.setSelectClauseNode(select);
    statement.setFromClauseNode(from);
    statement.setWhereClauseNode(where);
    AnonymousNode root = new AnonymousNode();
    root.appendNode(statement);

    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    PreparedSql sql = builder.build(statement, Function.identity());
    assertEquals("select * from aaa where  ddd = eee", sql.getRawSql());
  }

  public void testEmbeddedVariable_containsSingleQuote() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "aaa'"));
    String testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    try {
      new NodePreparedSqlBuilder(
              config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
          .build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2116, expected.getMessageResource());
    }
  }

  public void testEmbeddedVariable_containsSemicolon() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "aaa;bbb"));
    String testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    try {
      new NodePreparedSqlBuilder(
              config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
          .build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2117, expected.getMessageResource());
    }
  }

  public void testEmbeddedVariable_lineComment() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "aaa--bbb"));
    String testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    try {
      new NodePreparedSqlBuilder(
              config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
          .build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2122, expected.getMessageResource());
    }
  }

  public void testEmbeddedVariable_blockComment() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hoge"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    evaluator.add("orderBy", new Value(String.class, "aaa/*bbb"));
    String testSql =
        "select * from aaa where ename = /*name*/'aaa' and sal = /*salary*/-2000 /*#orderBy*/";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    try {
      new NodePreparedSqlBuilder(
              config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
          .build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2123, expected.getMessageResource());
    }
  }

  public void testEmbeddedVariable_issue_95() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("envPrefix", new Value(String.class, "prefix_"));
    String testSql = "select * from /*#envPrefix*/SCHEMA.TABLE";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    PreparedSql sql =
        new NodePreparedSqlBuilder(
                config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
            .build(sqlNode, Function.identity());
    assertEquals("select * from prefix_SCHEMA.TABLE", sql.getRawSql());
  }

  public void testLiteralVariable_containsSingleQuote() throws Exception {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("name", new Value(String.class, "hog'e"));
    evaluator.add("salary", new Value(BigDecimal.class, new BigDecimal(10000)));
    String testSql = "select * from aaa where ename = /*^name*/'aaa' and sal = /*salary*/-2000";
    SqlParser parser = new SqlParser(testSql);
    SqlNode sqlNode = parser.parse();
    try {
      new NodePreparedSqlBuilder(
              config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED)
          .build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2224, expected.getMessageResource());
    }
  }
}
