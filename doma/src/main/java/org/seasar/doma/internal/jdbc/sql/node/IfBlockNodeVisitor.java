package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface IfBlockNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitIfBlockNode(IfBlockNode node, P p);
}
