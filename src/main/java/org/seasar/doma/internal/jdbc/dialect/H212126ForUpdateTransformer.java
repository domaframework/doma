package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;

/** @author taedium */
public class H212126ForUpdateTransformer extends StandardForUpdateTransformer {

  public H212126ForUpdateTransformer(
      SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    super(forUpdateType, waitSeconds, aliases);
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    processed = true;

    ForUpdateClauseNode forUpdate = new ForUpdateClauseNode("for update");

    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(node.getSelectClauseNode());
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(node.getOrderByClauseNode());
    result.setForUpdateClauseNode(forUpdate);
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }
}
