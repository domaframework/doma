package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class WhereClauseNode extends AbstractClauseNode {

    public WhereClauseNode(String word) {
        super(word);
    }

    public WhereClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public WhereClauseNode copy() {
        WhereClauseNode clone = new WhereClauseNode(wordNode.copy());
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (WhereClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            WhereClauseNodeVisitor<R, P> v = WhereClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitWhereClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}
