package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface ParensNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitParensNode(ParensNode node, P p);
}
