package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class ExpandNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String alias;

    protected final String text;

    public ExpandNode(SqlLocation location, String alias, String text) {
        assertNotNull(location, alias, text);
        this.location = location;
        this.alias = alias;
        this.text = text;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public String getAlias() {
        return alias;
    }

    public String getText() {
        return text;
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
        return visitor.visitExpandNode(this, p);
    }

}
