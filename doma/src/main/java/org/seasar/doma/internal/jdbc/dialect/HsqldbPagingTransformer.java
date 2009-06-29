package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class HsqldbPagingTransformer extends StandardPagingTransformer {

    public HsqldbPagingTransformer(Integer offset, Integer limit) {
        super(offset, limit);
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        OrderByClauseNode orderBy = node.getOrderByClauseNode();
        if (orderBy == null) {
            orderBy = new OrderByClauseNode("");
        }
        orderBy.addNode(new FragmentNode(" limit "));
        if (limit >= 0) {
            orderBy.addNode(new FragmentNode(String.valueOf(limit)));
        } else {
            orderBy.addNode(new FragmentNode("0"));
        }
        if (offset >= 0) {
            orderBy.addNode(new FragmentNode(" offset "));
            orderBy.addNode(new FragmentNode(String.valueOf(offset)));
        }
        ForUpdateClauseNode originalForUpdate = node.getForUpdateClauseNode();
        if (originalForUpdate != null) {
            orderBy.addNode(new FragmentNode(" "));
        }

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(node.getSelectClauseNode());
        result.setFromClauseNode(node.getFromClauseNode());
        result.setWhereClauseNode(node.getWhereClauseNode());
        result.setGroupByClauseNode(node.getGroupByClauseNode());
        result.setHavingClauseNode(node.getHavingClauseNode());
        result.setOrderByClauseNode(orderBy);
        result.setForUpdateClauseNode(originalForUpdate);
        return result;
    }
}