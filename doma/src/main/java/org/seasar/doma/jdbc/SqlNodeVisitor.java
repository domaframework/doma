package org.seasar.doma.jdbc;


/**
 * @author taedium
 * 
 */
public interface SqlNodeVisitor<R, P> {

    R visitUnknownNode(SqlNode node, P p);

}
