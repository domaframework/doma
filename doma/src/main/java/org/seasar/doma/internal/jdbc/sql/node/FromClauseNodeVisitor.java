package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface FromClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitFromClauseNode(FromClauseNode node, P p);
}
