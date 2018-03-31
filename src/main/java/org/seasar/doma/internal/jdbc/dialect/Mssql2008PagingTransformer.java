package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

public class Mssql2008PagingTransformer extends StandardPagingTransformer {

  public Mssql2008PagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
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
    if (offset > 0) {
      return super.visitSelectStatementNode(node, p);
    }
    processed = true;

    return appendTopNode(node);
  }

  protected SqlNode appendTopNode(SelectStatementNode node) {
    var select = new SelectClauseNode(node.getSelectClauseNode().getWordNode());
    select.appendNode(new FragmentNode(" top (" + limit + ")"));
    for (var child : node.getSelectClauseNode().getChildren()) {
      select.appendNode(child);
    }

    var result = new SelectStatementNode();
    result.setSelectClauseNode(select);
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(node.getOrderByClauseNode());
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }
}
