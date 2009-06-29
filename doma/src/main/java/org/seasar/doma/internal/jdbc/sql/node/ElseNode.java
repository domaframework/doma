package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ElseNode extends AbstractSqlNode {

    protected final String text;

    public ElseNode(String text) {
        assertNotNull(text);
        this.text = text;
    }

    @Override
    public ElseNode copy() {
        ElseNode clone = new ElseNode(text);
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
        if (ElseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ElseNodeVisitor<R, P> v = ElseNodeVisitor.class.cast(visitor);
            return v.visitElseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(text);
        for (SqlNode child : children) {
            buf.append(child);
        }
        return buf.toString();
    }
}
