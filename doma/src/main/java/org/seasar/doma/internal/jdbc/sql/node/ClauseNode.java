package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public interface ClauseNode extends SqlNode {

    WordNode getWordNode();
}
