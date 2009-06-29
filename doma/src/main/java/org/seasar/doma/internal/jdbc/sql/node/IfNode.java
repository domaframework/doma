package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class IfNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String expression;

    protected final String text;

    public IfNode(SqlLocation location, String expression, String text) {
        assertNotNull(location, expression, text);
        this.location = location;
        this.expression = expression;
        this.text = text;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public IfNode copy() {
        IfNode clone = new IfNode(location, expression, text);
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
        if (IfNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            IfNodeVisitor<R, P> v = IfNodeVisitor.class.cast(visitor);
            return v.visitIfNode(this, p);
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
