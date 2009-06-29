package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface EndNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitEndNode(EndNode node, P p);
}
