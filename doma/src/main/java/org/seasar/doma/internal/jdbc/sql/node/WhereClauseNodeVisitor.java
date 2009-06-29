package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface WhereClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitWhereClauseNode(WhereClauseNode node, P p);
}
