package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

/**
 * @author nakamura-to
 * @since 2.3.0
 */
public interface WhereClauseAwareNode extends SqlNode {

    WhereClauseNode getWhereClauseNode();

    void setWhereClauseNode(WhereClauseNode whereClauseNode);

}
