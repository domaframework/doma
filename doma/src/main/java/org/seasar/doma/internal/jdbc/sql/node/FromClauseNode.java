package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class FromClauseNode extends AbstractClauseNode {

    public FromClauseNode(String word) {
        super(word);
    }

    public FromClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public FromClauseNode copy() {
        FromClauseNode clone = new FromClauseNode(wordNode.copy());
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
        if (FromClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            FromClauseNodeVisitor<R, P> v = FromClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitFromClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}
