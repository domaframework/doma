package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface AnonymousNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitAnonymousNode(AnonymousNode node, P p);
}
