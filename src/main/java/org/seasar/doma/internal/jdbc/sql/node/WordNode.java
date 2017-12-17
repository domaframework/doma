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
public class WordNode extends AbstractSqlNode {

    protected final String word;

    protected final boolean reserved;

    public WordNode(String word) {
        this(word, false);
    }

    public WordNode(String word, boolean reserved) {
        assertNotNull(word);
        this.word = word;
        this.reserved = reserved;
    }

    public String getWord() {
        return word;
    }

    public boolean isReserved() {
        return reserved;
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
        return visitor.visitWordNode(this, p);
    }

}
