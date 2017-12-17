package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * 
 * @author nakamura-to
 * @since 2.3.0
 */
public class UpdateStatementNode extends AbstractSqlNode implements WhereClauseAwareNode {

    protected UpdateClauseNode updateClauseNode;

    protected SetClauseNode setClauseNode;

    protected WhereClauseNode whereClauseNode;

    public UpdateClauseNode getUpdateClauseNode() {
        return updateClauseNode;
    }

    public void setUpdateClauseNode(UpdateClauseNode updateClauseNode) {
        this.updateClauseNode = updateClauseNode;
        appendNodeInternal(updateClauseNode);
    }

    public SetClauseNode getSetClauseNode() {
        return setClauseNode;
    }

    public void setSetClauseNode(SetClauseNode setClauseNode) {
        this.setClauseNode = setClauseNode;
        appendNodeInternal(setClauseNode);
    }

    @Override
    public WhereClauseNode getWhereClauseNode() {
        return whereClauseNode;
    }

    @Override
    public void setWhereClauseNode(WhereClauseNode whereClauseNode) {
        this.whereClauseNode = whereClauseNode;
        appendNodeInternal(whereClauseNode);
    }

    @Override
    public void appendNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");

    }

    protected void appendNodeInternal(SqlNode child) {
        if (child != null) {
            super.appendNode(child);
        }
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitUpdateStatementNode(this, p);
    }

}
