package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

public class SqlitePagingTransformer extends StandardPagingTransformer {

  protected static String MAXIMUM_LIMIT = String.valueOf(Long.MAX_VALUE);

  public SqlitePagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
    OrderByClauseNode orderBy = node.getOrderByClauseNode();
    if (originalOrderBy != null) {
      orderBy = new OrderByClauseNode(originalOrderBy.getWordNode());
      for (SqlNode child : originalOrderBy.getChildren()) {
        orderBy.appendNode(child);
      }
    } else {
      orderBy = new OrderByClauseNode("");
    }
    String offset = this.offset <= 0 ? "0" : String.valueOf(this.offset);
    String limit = this.limit <= 0 ? MAXIMUM_LIMIT : String.valueOf(this.limit);
    orderBy.appendNode(new FragmentNode(" limit "));
    orderBy.appendNode(new FragmentNode(limit));
    orderBy.appendNode(new FragmentNode(" offset "));
    orderBy.appendNode(new FragmentNode(offset));

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
