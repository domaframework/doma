package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;


public class OtherNode extends AbstractSqlNode {

    protected static final Map<String, OtherNode> otherNodeMap = new HashMap<String, OtherNode>();
    static {
        otherNodeMap.put(" ", new OtherNode(" "));
        otherNodeMap.put(",", new OtherNode(","));
        otherNodeMap.put("=", new OtherNode("="));
        otherNodeMap.put(">", new OtherNode(">"));
        otherNodeMap.put("<", new OtherNode("<"));
        otherNodeMap.put(">=", new OtherNode(">="));
        otherNodeMap.put("<=", new OtherNode("<="));
    }

    protected final String other;

    private OtherNode(String other) {
        assertNotNull(other);
        this.other = other;
    }

    public String getOther() {
        return other;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public OtherNode copy() {
        return this;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (OtherNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            OtherNodeVisitor<R, P> v = OtherNodeVisitor.class.cast(visitor);
            return v.visitOtherNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        return other;
    }

    public static OtherNode of(String other) {
        OtherNode otherNode = otherNodeMap.get(other);
        if (otherNode != null) {
            return otherNode;
        }
        return new OtherNode(other);
    }

}
