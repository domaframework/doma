package org.seasar.doma.jdbc;

import org.seasar.doma.internal.jdbc.sql.node.*;

/**
 * A visitor for {@link SqlNode}.
 *
 * <p>The implementation class must be thread safe.
 *
 * @param <R> the result type
 * @param <P> the parameter type
 */
public interface SqlNodeVisitor<R, P> {

  R visitAnonymousNode(AnonymousNode node, P p);

  R visitBindVariableNode(BindVariableNode node, P p);

  R visitCommentNode(CommentNode node, P p);

  R visitElseifNode(ElseifNode node, P p);

  R visitElseNode(ElseNode node, P p);

  R visitEmbeddedVariableNode(EmbeddedVariableNode node, P p);

  R visitEndNode(EndNode node, P p);

  R visitEolNode(EolNode node, P p);

  R visitExpandNode(ExpandNode node, P p);

  R visitForBlockNode(ForBlockNode node, P p);

  R visitForNode(ForNode node, P p);

  R visitForUpdateClauseNode(ForUpdateClauseNode node, P p);

  R visitFragmentNode(FragmentNode node, P p);

  R visitFromClauseNode(FromClauseNode node, P p);

  R visitGroupByClauseNode(GroupByClauseNode node, P p);

  R visitHavingClauseNode(HavingClauseNode node, P p);

  R visitIfBlockNode(IfBlockNode node, P p);

  R visitIfNode(IfNode node, P p);

  R visitLiteralVariableNode(LiteralVariableNode node, P p);

  R visitLogicalOperatorNode(LogicalOperatorNode node, P p);

  R visitOptionClauseNode(OptionClauseNode node, P p);

  R visitOrderByClauseNode(OrderByClauseNode node, P p);

  R visitOtherNode(OtherNode node, P p);

  R visitParensNode(ParensNode node, P p);

  R visitPopulateNode(PopulateNode node, P p);

  R visitSelectClauseNode(SelectClauseNode node, P p);

  R visitSelectStatementNode(SelectStatementNode node, P p);

  R visitSetClauseNode(SetClauseNode node, P p);

  R visitUpdateClauseNode(UpdateClauseNode node, P p);

  R visitUpdateStatementNode(UpdateStatementNode node, P p);

  R visitWhereClauseNode(WhereClauseNode node, P p);

  R visitWhitespaceNode(WhitespaceNode node, P p);

  R visitWordNode(WordNode node, P p);
}
