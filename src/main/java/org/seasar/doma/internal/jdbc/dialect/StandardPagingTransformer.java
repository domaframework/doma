package org.seasar.doma.internal.jdbc.dialect;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.*;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class StandardPagingTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  private final AliasReplacer replacer = new AliasReplacer();

  protected final long offset;

  protected final long limit;

  protected boolean processed;

  public StandardPagingTransformer(long offset, long limit) {
    assertTrue(offset >= 0 || limit >= 0);
    this.offset = offset;
    this.limit = limit;
  }

  public SqlNode transform(SqlNode sqlNode) {
    AnonymousNode result = new AnonymousNode();
    for (SqlNode child : sqlNode.getChildren()) {
      result.appendNode(child.accept(this, null));
    }
    return result;
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
    if (originalOrderBy == null) {
      throw new JdbcException(Message.DOMA2201);
    }
    SelectStatementNode subStatement = new SelectStatementNode();
    subStatement.setSelectClauseNode(node.getSelectClauseNode());
    subStatement.setFromClauseNode(node.getFromClauseNode());
    subStatement.setWhereClauseNode(node.getWhereClauseNode());
    subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
    subStatement.setHavingClauseNode(node.getHavingClauseNode());

    OrderByClauseNode orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
    for (SqlNode child : originalOrderBy.getChildren()) {
      SqlNode newChild = child.accept(replacer, null);
      orderBy.appendNode(newChild);
    }

    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(new FragmentNode(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(new FragmentNode(" ( select temp_.*, row_number() over( "));
    from.appendNode(orderBy);
    from.appendNode(new FragmentNode(" ) as " + ROWNUMBER_COLUMN_NAME + " from ( "));
    from.appendNode(subStatement);
    from.appendNode(new FragmentNode(") as temp_ ) as temp2_ "));
    WhereClauseNode where = new WhereClauseNode("where");
    where.appendNode(new FragmentNode(" "));
    if (offset >= 0) {
      where.appendNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " > "));
      where.appendNode(new FragmentNode(String.valueOf(offset)));
    }
    if (limit > 0) {
      if (offset >= 0) {
        where.appendNode(new FragmentNode(" and "));
      }
      long bias = offset < 0 ? 0 : offset;
      where.appendNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " <= "));
      where.appendNode(new FragmentNode(String.valueOf(bias + limit)));
    }

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(select);
    result.setFromClauseNode(from);
    result.setWhereClauseNode(where);
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }

  @Override
  protected SqlNode defaultAction(SqlNode node, Void p) {
    return node;
  }

  protected static class AliasReplacer extends SimpleSqlNodeVisitor<SqlNode, Void> {

    @Override
    public SqlNode visitWordNode(WordNode node, Void aVoid) {
      String word = node.getWord();
      String[] names = word.split("\\.");
      if (names.length == 2) {
        return new WordNode("temp_." + names[1]);
      }
      return node;
    }

    @Override
    public SqlNode visitIfBlockNode(IfBlockNode node, Void aVoid) {
      IfBlockNode ifBlockNode = new IfBlockNode();
      IfNode originalIfNode = node.getIfNode();
      IfNode ifNode =
          buildNode(
              originalIfNode, o -> new IfNode(o.getLocation(), o.getExpression(), o.getText()));
      ifBlockNode.setIfNode(ifNode);
      for (ElseifNode originalElseifNode : node.getElseifNodes()) {
        ElseifNode elseifNode =
            buildNode(
                originalElseifNode,
                o -> new ElseifNode(o.getLocation(), o.getExpression(), o.getText()));
        ifBlockNode.addElseifNode(elseifNode);
      }
      ElseNode originalElseNode = node.getElseNode();
      if (originalElseNode != null) {
        ElseNode elseNode = buildNode(originalElseNode, o -> new ElseNode(o.getText()));
        ifBlockNode.setElseNode(elseNode);
      }
      EndNode originalEndNode = node.getEndNode();
      if (originalEndNode != null) {
        EndNode endNode = buildNode(originalEndNode, o -> new EndNode(o.getText()));
        ifBlockNode.setEndNode(endNode);
      }
      return ifBlockNode;
    }

    @Override
    public SqlNode visitForBlockNode(ForBlockNode node, Void aVoid) {
      ForBlockNode forBlockNode = new ForBlockNode();
      ForNode originalForNode = node.getForNode();
      ForNode forNode =
          buildNode(
              originalForNode,
              o -> new ForNode(o.getLocation(), o.getIdentifier(), o.getExpression(), o.getText()));
      forBlockNode.setForNode(forNode);
      EndNode originalEndNode = node.getEndNode();
      if (originalEndNode != null) {
        EndNode endNode = buildNode(originalEndNode, o -> new EndNode(o.getText()));
        forBlockNode.setEndNode(endNode);
      }
      return forBlockNode;
    }

    private <NODE extends AppendableSqlNode> NODE buildNode(
        NODE originalNode, Function<NODE, NODE> factory) {
      NODE newNode = factory.apply(originalNode);
      for (SqlNode child : originalNode.getChildren()) {
        SqlNode newChild = child.accept(this, null);
        newNode.appendNode(newChild);
      }
      return newNode;
    }

    @Override
    protected SqlNode defaultAction(SqlNode node, Void p) {
      return node;
    }
  }
}
