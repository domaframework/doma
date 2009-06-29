package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ForUpdateClauseNode extends AbstractClauseNode {

    public ForUpdateClauseNode(String word) {
        super(word);
    }

    public ForUpdateClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public ForUpdateClauseNode copy() {
        ForUpdateClauseNode clone = new ForUpdateClauseNode(wordNode.copy());
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
        if (ForUpdateClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ForUpdateClauseNodeVisitor<R, P> v = ForUpdateClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitForUpdateClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}
