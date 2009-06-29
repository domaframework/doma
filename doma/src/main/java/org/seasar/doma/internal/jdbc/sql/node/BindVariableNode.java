package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class BindVariableNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String variableName;

    protected final String text;

    protected WordNode wordNode;

    protected ParensNode parensNode;

    public BindVariableNode(SqlLocation location, String variableName,
            String text) {
        assertNotNull(location, variableName, text);
        this.location = location;
        this.variableName = variableName;
        this.text = text;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getText() {
        return text;
    }

    @Override
    public BindVariableNode copy() {
        BindVariableNode clone = new BindVariableNode(location, variableName,
                text);
        if (wordNode != null) {
            clone.wordNode = wordNode.copy();
        }
        if (parensNode != null) {
            clone.parensNode = parensNode.copy();
        }
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
        if (BindVariableNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            BindVariableNodeVisitor<R, P> v = BindVariableNodeVisitor.class
                    .cast(visitor);
            return v.visitBindVariableNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    public WordNode getWordNode() {
        return wordNode;
    }

    public void setWordNode(WordNode wordNode) {
        this.wordNode = wordNode;
    }

    public ParensNode getParensNode() {
        return parensNode;
    }

    public void setParensNode(ParensNode parensNode) {
        this.parensNode = parensNode;
    }

    public boolean isWordNodeIgnored() {
        return wordNode != null;
    }

    public boolean isParensNodeIgnored() {
        return parensNode != null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(text);
        for (SqlNode child : children) {
            buf.append(child);
        }
        if (wordNode != null) {
            buf.append(wordNode);
        } else if (parensNode != null) {
            buf.append(parensNode);
        }
        return buf.toString();
    }

}
