package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class HavingClauseNode extends AbstractClauseNode {

    public HavingClauseNode(String word) {
        super(word);
    }

    public HavingClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public HavingClauseNode copy() {
        HavingClauseNode clone = new HavingClauseNode(wordNode.copy());
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
        if (HavingClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            HavingClauseNodeVisitor<R, P> v = HavingClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitHavingClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}