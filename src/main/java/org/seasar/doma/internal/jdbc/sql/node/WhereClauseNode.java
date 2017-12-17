package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
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
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitWhereClauseNode(this, p);
    }

}
