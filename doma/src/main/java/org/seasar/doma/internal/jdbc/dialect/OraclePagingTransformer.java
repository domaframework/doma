package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class OraclePagingTransformer extends StandardPagingTransformer {

    public OraclePagingTransformer(int offset, int limit) {
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
        select.addNode(new FragmentNode(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from.addNode(new FragmentNode(
                " ( select temp_.*, rownum rownumber_ from ( "));
        from.addNode(subStatement);
        from.addNode(new FragmentNode(" ) temp_ ) "));
        WhereClauseNode where = new WhereClauseNode("where");
        where.addNode(new FragmentNode(" "));
        if (offset >= 0) {
            where.addNode(new FragmentNode("rownumber_ > "));
            where.addNode(new FragmentNode(String.valueOf(offset)));
        }
        if (limit > 0) {
            if (offset >= 0) {
                where.addNode(new FragmentNode(" and "));
            }
            where.addNode(new FragmentNode("rownumber_ <= "));
            int bias = offset < 0 ? 0 : offset;
            where.addNode(new FragmentNode(String.valueOf(bias + limit)));
        }
        ForUpdateClauseNode originalForUpdate = node.getForUpdateClauseNode();
        if (originalForUpdate != null) {
            where.addNode(new FragmentNode(" "));
        }

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(select);
        result.setFromClauseNode(from);
        result.setWhereClauseNode(where);
        result.setForUpdateClauseNode(originalForUpdate);
        return result;
    }
}
