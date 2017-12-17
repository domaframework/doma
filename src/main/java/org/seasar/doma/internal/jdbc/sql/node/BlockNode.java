package org.seasar.doma.internal.jdbc.sql.node;

/**
 * @author taedium
 * 
 */
public interface BlockNode extends AppendableSqlNode {

    void setEndNode(EndNode endNode);
}
