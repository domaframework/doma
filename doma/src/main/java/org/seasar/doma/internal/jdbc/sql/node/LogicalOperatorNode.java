package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class LogicalOperatorNode extends AbstractSqlNode {

    protected final WordNode wordNode;

    public LogicalOperatorNode(String word) {
        this(new WordNode(word));
    }

    protected LogicalOperatorNode(WordNode wordNode) {
        this.wordNode = wordNode;
    }

    public WordNode getWordNode() {
        return wordNode;
    }

    @Override
    public LogicalOperatorNode copy() {
        LogicalOperatorNode clone = new LogicalOperatorNode(wordNode.copy());
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
        if (LogicalOperatorNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            LogicalOperatorNodeVisitor<R, P> v = LogicalOperatorNodeVisitor.class
                    .cast(visitor);
            return v.visitLogicalOperatorNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(wordNode);
        for (SqlNode child : children) {
            buf.append(child);
        }
        return buf.toString();
    }
}