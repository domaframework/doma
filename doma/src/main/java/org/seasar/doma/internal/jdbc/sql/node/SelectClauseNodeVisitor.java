package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface SelectClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitSelectClauseNode(SelectClauseNode node, P p);
}
