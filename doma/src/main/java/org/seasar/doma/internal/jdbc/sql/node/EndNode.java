package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class EndNode extends AbstractSqlNode {

    protected final String text;

    public EndNode(String text) {
        this.text = text;
    }

    @Override
    public EndNode copy() {
        EndNode clone = new EndNode(text);
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (EndNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            EndNodeVisitor<R, P> v = EndNodeVisitor.class.cast(visitor);
            return v.visitEndNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        return text;
    }

}
