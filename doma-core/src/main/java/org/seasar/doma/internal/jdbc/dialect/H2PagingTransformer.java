package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

public class H2PagingTransformer extends H212126PagingTransformer {

  public H2PagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
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
    orderBy.appendNode(new FragmentNode(" limit "));
    if (limit > 0) {
      orderBy.appendNode(new FragmentNode(String.valueOf(limit)));
    } else {
      orderBy.appendNode(new FragmentNode("-1"));
    }
    if (offset >= 0) {
      orderBy.appendNode(new FragmentNode(" offset "));
      orderBy.appendNode(new FragmentNode(String.valueOf(offset)));
    }

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
