package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class SelectClauseNode extends AbstractClauseNode {

    public SelectClauseNode(String word) {
        super(word);
    }

    public SelectClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public SelectClauseNode copy() {
        SelectClauseNode clone = new SelectClauseNode(wordNode.copy());
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
        if (SelectClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            SelectClauseNodeVisitor<R, P> v = SelectClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitSelectClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}