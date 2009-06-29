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
public class WordNode extends AbstractSqlNode {

    protected final String word;

    public WordNode(String word) {
        assertNotNull(word);
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public WordNode copy() {
        return this;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (WordNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            WordNodeVisitor<R, P> v = WordNodeVisitor.class.cast(visitor);
            return v.visitWordNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        return word;
    }

}
