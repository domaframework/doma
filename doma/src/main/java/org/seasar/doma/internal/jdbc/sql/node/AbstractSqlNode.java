package org.seasar.doma.internal.jdbc.sql.node;

import java.util.LinkedList;
import java.util.List;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;


/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlNode implements SqlNode {

    protected final LinkedList<SqlNode> children = new LinkedList<SqlNode>();

    public void addNode(SqlNode child) {
        if (child == null) {
            throw new DomaIllegalArgumentException("child", child);
        }
        children.add(child);
    }

    @Override
    public List<SqlNode> getChildren() {
        return children;
    }

}
