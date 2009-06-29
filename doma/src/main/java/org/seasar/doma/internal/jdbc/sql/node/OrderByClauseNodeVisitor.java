package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface OrderByClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitOrderByClauseNode(OrderByClauseNode node, P p);
}
