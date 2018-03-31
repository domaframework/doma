package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.*;
import org.seasar.doma.jdbc.SqlNode;

public class StandardCountGettingTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  protected boolean processed;

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

    SelectStatementNode subStatement = new SelectStatementNode();
    subStatement.setSelectClauseNode(node.getSelectClauseNode());
    subStatement.setFromClauseNode(node.getFromClauseNode());
    subStatement.setWhereClauseNode(node.getWhereClauseNode());
    subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
    subStatement.setHavingClauseNode(node.getHavingClauseNode());

    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(new FragmentNode(" count(*) "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(new FragmentNode(" ( "));
    from.appendNode(subStatement);
    from.appendNode(new FragmentNode(") t_"));

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(select);
    result.setFromClauseNode(from);
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }

  @Override
  public SqlNode defaultAction(SqlNode node, Void p) {
    return node;
  }
}
