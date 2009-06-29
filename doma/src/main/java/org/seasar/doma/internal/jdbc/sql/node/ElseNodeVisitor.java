package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface ElseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitElseNode(ElseNode node, P p);
}
