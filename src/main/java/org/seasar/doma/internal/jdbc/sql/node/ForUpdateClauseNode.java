package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
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
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitForUpdateClauseNode(this, p);
    }

}
