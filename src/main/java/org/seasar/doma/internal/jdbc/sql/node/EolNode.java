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
public class EolNode extends AbstractSqlNode {

    protected final String eol;

    public EolNode(String eol) {
        assertNotNull(eol);
        this.eol = eol;
    }

    public String getEol() {
        return eol;
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
        return visitor.visitEolNode(this, p);
    }

}
