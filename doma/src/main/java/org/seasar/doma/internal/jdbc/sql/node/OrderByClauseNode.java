package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class OrderByClauseNode extends AbstractClauseNode {

    public OrderByClauseNode(String word) {
        super(word);
    }

    public OrderByClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public OrderByClauseNode copy() {
        OrderByClauseNode clone = new OrderByClauseNode(wordNode.copy());
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
        if (OrderByClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            OrderByClauseNodeVisitor<R, P> v = OrderByClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitOrderByClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}
