package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class MysqlCountGettingTransformer extends StandardCountGettingTransformer {

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        SelectClauseNode select = new SelectClauseNode("select");
        select.appendNode(new FragmentNode(" found_rows()"));

        SelectStatementNode result = new SelectStatementNode();
        result.setSelectClauseNode(select);
        return result;
    }
}