package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface ElseifNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitElseifNode(ElseifNode node, P p);
}
