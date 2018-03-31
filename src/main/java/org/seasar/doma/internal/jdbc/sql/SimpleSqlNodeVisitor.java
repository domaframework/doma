package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.internal.jdbc.sql.node.*;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/** @author nakamura-to */
public class SimpleSqlNodeVisitor<R, P> implements SqlNodeVisitor<R, P> {

  protected R defaultAction(SqlNode node, P p) {
    return null;
  }

  @Override
  public R visitAnonymousNode(AnonymousNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitBindVariableNode(BindVariableNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitCommentNode(CommentNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitElseifNode(ElseifNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitElseNode(ElseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitEmbeddedVariableNode(EmbeddedVariableNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitEndNode(EndNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitEolNode(EolNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitExpandNode(ExpandNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitForBlockNode(ForBlockNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitForNode(ForNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitForUpdateClauseNode(ForUpdateClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitFragmentNode(FragmentNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitFromClauseNode(FromClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitGroupByClauseNode(GroupByClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitHavingClauseNode(HavingClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitIfBlockNode(IfBlockNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitIfNode(IfNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitLiteralVariableNode(LiteralVariableNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitLogicalOperatorNode(LogicalOperatorNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitOptionClauseNode(OptionClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitOrderByClauseNode(OrderByClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitOtherNode(OtherNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitParensNode(ParensNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitPopulateNode(PopulateNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitSelectClauseNode(SelectClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitSelectStatementNode(SelectStatementNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitSetClauseNode(SetClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitUpdateClauseNode(UpdateClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitUpdateStatementNode(UpdateStatementNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitWhereClauseNode(WhereClauseNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitWhitespaceNode(WhitespaceNode node, P p) {
    return defaultAction(node, p);
  }

  @Override
  public R visitWordNode(WordNode node, P p) {
    return defaultAction(node, p);
  }
}
