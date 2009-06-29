package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ElseifNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String expression;

    protected final String text;

    public ElseifNode(SqlLocation location, String expression, String text) {
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
    public ElseifNode copy() {
        ElseifNode clone = new ElseifNode(location, expression, text);
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
        if (ElseifNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ElseifNodeVisitor<R, P> v = ElseifNodeVisitor.class.cast(visitor);
            return v.visitElseifNode(this, p);
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
