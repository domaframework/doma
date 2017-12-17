package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class Mssql2008ForUpdateTransformer extends StandardForUpdateTransformer {

    public Mssql2008ForUpdateTransformer(SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        super(forUpdateType, waitSeconds, aliases);
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        StringBuilder buf = new StringBuilder(100).append("with (updlock, rowlock");
        if (forUpdateType == SelectForUpdateType.NOWAIT) {
            buf.append(", nowait");
        }
        buf.append(") ");
        FromClauseNode originalFrom = node.getFromClauseNode();
        FromClauseNode from = new FromClauseNode(originalFrom.getWordNode());
        for (SqlNode child : originalFrom.getChildren()) {
            from.appendNode(child);
        }
        from.appendNode(new FragmentNode(buf.toString()));

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(node.getSelectClauseNode());
        result.setFromClauseNode(from);
        result.setWhereClauseNode(node.getWhereClauseNode());
        result.setGroupByClauseNode(node.getGroupByClauseNode());
        result.setHavingClauseNode(node.getHavingClauseNode());
        result.setOrderByClauseNode(node.getOrderByClauseNode());
        result.setOptionClauseNode(node.getOptionClauseNode());
        return result;
    }
}
