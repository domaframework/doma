package org.seasar.doma.internal.jdbc.dialect;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNodeVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class StandardForUpdateTransformer implements
        SelectStatementNodeVisitor<SqlNode, Void> {

    protected final SelectForUpdateType forUpdateType;

    protected int waitSeconds;

    protected String[] aliases;

    protected boolean processed;

    public StandardForUpdateTransformer(SelectForUpdateType forUpdateType,
            int waitSeconds, String... aliases) {
        assertNotNull(forUpdateType);
        this.forUpdateType = forUpdateType;
        this.waitSeconds = waitSeconds;
        this.aliases = aliases;
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
        return node;
    }

    @Override
    public SqlNode visitUnknownNode(SqlNode node, Void p) {
        return node;
    }

}
