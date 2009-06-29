package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public interface ForUpdateClauseNodeVisitor<R, P> extends SqlNodeVisitor<R, P> {

    R visitForUpdateClauseNode(ForUpdateClauseNode node, P p);
}
