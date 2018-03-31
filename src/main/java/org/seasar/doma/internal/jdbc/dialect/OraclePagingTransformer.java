package org.seasar.doma.internal.jdbc.dialect;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;

import org.seasar.doma.internal.jdbc.sql.node.*;
import org.seasar.doma.jdbc.SqlNode;

public class OraclePagingTransformer extends StandardPagingTransformer {

  public OraclePagingTransformer(long offset, long limit) {
    super(offset, limit);
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
    subStatement.setOrderByClauseNode(node.getOrderByClauseNode());

    SelectClauseNode select = new SelectClauseNode("select");
    select.appendNode(new FragmentNode(" * "));
    FromClauseNode from = new FromClauseNode("from");
    from.appendNode(
        new FragmentNode(" ( select temp_.*, rownum " + ROWNUMBER_COLUMN_NAME + " from ( "));
    from.appendNode(subStatement);
    from.appendNode(new FragmentNode(" ) temp_ ) "));
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
      where.appendNode(new FragmentNode(ROWNUMBER_COLUMN_NAME + " <= "));
      long bias = offset < 0 ? 0 : offset;
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
}
