package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
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
    public void appendNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitFragmentNode(this, p);
    }

}
