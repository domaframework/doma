package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class GroupByClauseNode extends AbstractClauseNode {

    public GroupByClauseNode(String word) {
        super(word);
    }

    public GroupByClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @Override
    public GroupByClauseNode copy() {
        GroupByClauseNode clone = new GroupByClauseNode(wordNode.copy());
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public WordNode getWordNode() {
        return wordNode;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (GroupByClauseNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            GroupByClauseNodeVisitor<R, P> v = GroupByClauseNodeVisitor.class
                    .cast(visitor);
            return v.visitGroupByClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

}
