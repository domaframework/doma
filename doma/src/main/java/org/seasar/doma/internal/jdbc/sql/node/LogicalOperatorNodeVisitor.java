package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface LogicalOperatorNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitLogicalOperatorNode(LogicalOperatorNode node, P p);
}
