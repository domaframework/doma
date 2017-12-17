package org.seasar.doma.internal.jdbc.sql.node;

/**
 * @author taedium
 * 
 */
public interface ClauseNode extends AppendableSqlNode {

    WordNode getWordNode();
}
