package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

public class Db2PagingTransformer extends StandardPagingTransformer {

  public Db2PagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
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
    if (offset > 0) {
      return super.visitSelectStatementNode(node, p);
    }
    processed = true;

    OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
    OrderByClauseNode orderBy;
    if (originalOrderBy != null) {
      orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
      for (SqlNode child : originalOrderBy.getChildren()) {
        orderBy.appendNode(child);
      }
    } else {
      orderBy = new OrderByClauseNode("");
    }
    orderBy.appendNode(new FragmentNode(" fetch first " + limit + " rows only"));

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(node.getSelectClauseNode());
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(orderBy);
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }
}
