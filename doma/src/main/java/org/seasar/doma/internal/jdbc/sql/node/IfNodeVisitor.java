package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface IfNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitIfNode(IfNode node, P p);
}
