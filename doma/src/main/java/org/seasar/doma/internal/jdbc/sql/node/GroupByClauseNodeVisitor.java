package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface GroupByClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitGroupByClauseNode(GroupByClauseNode node, P p);
}
