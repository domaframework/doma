package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface OtherNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitOtherNode(OtherNode node, P p);
}
