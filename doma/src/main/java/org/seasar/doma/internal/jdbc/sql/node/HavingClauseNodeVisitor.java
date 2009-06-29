package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface HavingClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitHavingClauseNode(HavingClauseNode node, P p);
}
