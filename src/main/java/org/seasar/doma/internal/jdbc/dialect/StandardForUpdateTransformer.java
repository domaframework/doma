package org.seasar.doma.internal.jdbc.dialect;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class StandardForUpdateTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

    protected final SelectForUpdateType forUpdateType;

    protected final int waitSeconds;

    protected final String[] aliases;

    protected boolean processed;

    public StandardForUpdateTransformer(SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        assertNotNull(forUpdateType);
        this.forUpdateType = forUpdateType;
        this.waitSeconds = waitSeconds;
        this.aliases = aliases;
    }

    public SqlNode transform(SqlNode sqlNode) {
        AnonymousNode result = new AnonymousNode();
        for (SqlNode child : sqlNode.getChildren()) {
            result.appendNode(child.accept(this, null));
        }
        return result;
    }

    @Override
    protected SqlNode defaultAction(SqlNode node, Void p) {
        return node;
    }

}
