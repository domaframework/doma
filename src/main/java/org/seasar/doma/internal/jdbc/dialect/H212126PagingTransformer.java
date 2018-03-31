package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * H2の古いバージョン1.2.126で稼動実績があるトランスフォーマーです。
 *
 * @author taedium
 */
public class H212126PagingTransformer extends StandardPagingTransformer {

  public H212126PagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    var originalOrderBy = node.getOrderByClauseNode();
    OrderByClauseNode orderBy;
    if (originalOrderBy != null) {
      orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
      for (var child : originalOrderBy.getChildren()) {
        orderBy.appendNode(child);
      }
    } else {
      orderBy = new OrderByClauseNode("");
    }
    orderBy.appendNode(new FragmentNode(" limit "));
    if (limit >= 0) {
      orderBy.appendNode(new FragmentNode(String.valueOf(limit)));
    } else {
      orderBy.appendNode(new FragmentNode("0"));
    }
    if (offset >= 0) {
      orderBy.appendNode(new FragmentNode(" offset "));
      orderBy.appendNode(new FragmentNode(String.valueOf(offset)));
    }

    var result = new SelectStatementNode();
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
