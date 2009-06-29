package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class FragmentNode extends AbstractSqlNode {

    protected final String fragment;

    public FragmentNode(String fragment) {
        assertNotNull(fragment);
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public FragmentNode copy() {
        return this;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (FragmentNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            FragmentNodeVisitor<R, P> v = FragmentNodeVisitor.class
                    .cast(visitor);
            return v.visitFragmentNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        return fragment;
    }
}
