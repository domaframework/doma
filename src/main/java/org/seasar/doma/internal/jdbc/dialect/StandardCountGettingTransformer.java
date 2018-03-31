package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

public class StandardCountGettingTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  protected boolean processed;

  public SqlNode transform(SqlNode sqlNode) {
    var result = new AnonymousNode();
    for (var child : sqlNode.getChildren()) {
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

    var subStatement = new SelectStatementNode();
    subStatement.setSelectClauseNode(node.getSelectClauseNode());
    subStatement.setFromClauseNode(node.getFromClauseNode());
    subStatement.setWhereClauseNode(node.getWhereClauseNode());
    subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
    subStatement.setHavingClauseNode(node.getHavingClauseNode());

    var select = new SelectClauseNode("select");
    select.appendNode(new FragmentNode(" count(*) "));
    var from = new FromClauseNode("from");
    from.appendNode(new FragmentNode(" ( "));
    from.appendNode(subStatement);
    from.appendNode(new FragmentNode(") t_"));

    var result = new SelectStatementNode();
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
