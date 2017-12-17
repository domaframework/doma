package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class BindVariableNode extends ValueNode {

    public BindVariableNode(SqlLocation location, String variableName, String text) {
        super(location, variableName, text);
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitBindVariableNode(this, p);
    }

}
