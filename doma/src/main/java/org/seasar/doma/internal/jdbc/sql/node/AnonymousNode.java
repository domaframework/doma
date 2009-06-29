package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class AnonymousNode extends AbstractSqlNode {

    public AnonymousNode() {
    }

    @Override
    public AnonymousNode copy() {
        AnonymousNode clone = new AnonymousNode();
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AnonymousNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AnonymousNodeVisitor<R, P> v = AnonymousNodeVisitor.class
                    .cast(visitor);
            return v.visitAnonymousNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (SqlNode child : children) {
            buf.append(child);
        }
        return buf.toString();
    }
}
