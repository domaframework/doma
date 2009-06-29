package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface BindVariableNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitBindVariableNode(BindVariableNode node, P p);
}
