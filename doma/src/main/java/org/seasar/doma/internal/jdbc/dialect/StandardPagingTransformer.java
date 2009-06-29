package org.seasar.doma.internal.jdbc.dialect;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class StandardPagingTransformer implements
        SelectStatementNodeVisitor<SqlNode, Void> {

    protected final int offset;

    protected final int limit;

    protected boolean processed;

    public StandardPagingTransformer(int offset, int limit) {
        assertTrue(offset >= 0 || limit >= 0);
        this.offset = offset;
        this.limit = limit;
    }

    public SqlNode transform(SqlNode sqlNode) {
        AnonymousNode result = new AnonymousNode();
        for (SqlNode child : sqlNode.getChildren()) {
            result.addNode(child.accept(this, null));
        }
        return result;
    }

    @Override
    public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
        if (processed) {
            return node;
        }
        processed = true;

        OrderByClauseNode originalOrderBy = node.getOrderByClauseNode();
        if (originalOrderBy == null) {
            throw new JdbcException(MessageCode.DOMA2201);
        }
        SelectStatementNode subStatement = new SelectStatementNode();
        subStatement.setSelectClauseNode(node.getSelectClauseNode());
        subStatement.setFromClauseNode(node.getFromClauseNode());
        subStatement.setWhereClauseNode(node.getWhereClauseNode());
        subStatement.setGroupByClauseNode(node.getGroupByClauseNode());
        subStatement.setHavingClauseNode(node.getHavingClauseNode());

        OrderByClauseNode orderBy = new OrderByClauseNode(originalOrderBy
                .getWordNode());
        for (SqlNode child : originalOrderBy.getChildren()) {
            if (WordNode.class.isInstance(child)) {
                WordNode wordNode = WordNode.class.cast(child);
                String word = wordNode.getWord();
                String[] names = word.split("\\.");
                if (names.length == 2) {
                    orderBy.addNode(new WordNode("temp_." + names[1]));
                } else {
                    orderBy.addNode(child);
                }
            } else {
                orderBy.addNode(child);
            }
        }

        SelectClauseNode select = new SelectClauseNode("select");
        select.addNode(new FragmentNode(" * "));
        FromClauseNode from = new FromClauseNode("from");
        from
                .addNode(new FragmentNode(
                        " ( select temp_.*, row_number() over( "));
        from.addNode(orderBy);
        from.addNode(new FragmentNode(" ) as rownumber_ from ( "));
        from.addNode(subStatement);
        from.addNode(new FragmentNode(") as temp_ ) as temp2_ "));
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
            int bias = offset < 0 ? 0 : offset;
            where.addNode(new FragmentNode("rownumber_ <= "));
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

    @Override
    public SqlNode visitUnknownNode(SqlNode node, Void p) {
        return node;
    }

}