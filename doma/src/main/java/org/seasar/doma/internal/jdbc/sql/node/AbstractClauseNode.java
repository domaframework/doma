package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractClauseNode extends AbstractSqlNode implements
        ClauseNode {

    protected final WordNode wordNode;

    protected AbstractClauseNode(String word) {
        this(new WordNode(word));
    }

    protected AbstractClauseNode(WordNode wordNode) {
        this.wordNode = wordNode;
    }

    @Override
    public WordNode getWordNode() {
        return wordNode;
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
