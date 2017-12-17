package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class LiteralVariableNode extends ValueNode {

    public LiteralVariableNode(SqlLocation location, String variableName, String text) {
        super(location, variableName, text);
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitLiteralVariableNode(this, p);
    }

}
