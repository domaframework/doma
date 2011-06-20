/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class SelectStatementNode extends AbstractSqlNode {

    protected SelectClauseNode selectClauseNode;

    protected FromClauseNode fromClauseNode;

    protected WhereClauseNode whereClauseNode;

    protected GroupByClauseNode groupByClauseNode;

    protected HavingClauseNode havingClauseNode;

    protected OrderByClauseNode orderByClauseNode;

    protected ForUpdateClauseNode forUpdateClauseNode;

    public SelectClauseNode getSelectClauseNode() {
        return selectClauseNode;
    }

    public void setSelectClauseNode(SelectClauseNode selectClauseNode) {
        this.selectClauseNode = selectClauseNode;
        addNodeInternal(selectClauseNode);
    }

    public FromClauseNode getFromClauseNode() {
        return fromClauseNode;
    }

    public void setFromClauseNode(FromClauseNode fromClauseNode) {
        this.fromClauseNode = fromClauseNode;
        addNodeInternal(fromClauseNode);
    }

    public WhereClauseNode getWhereClauseNode() {
        return whereClauseNode;
    }

    public void setWhereClauseNode(WhereClauseNode whereClauseNode) {
        this.whereClauseNode = whereClauseNode;
        addNodeInternal(whereClauseNode);
    }

    public GroupByClauseNode getGroupByClauseNode() {
        return groupByClauseNode;
    }

    public void setGroupByClauseNode(GroupByClauseNode groupByClauseNode) {
        this.groupByClauseNode = groupByClauseNode;
        addNodeInternal(groupByClauseNode);
    }

    public HavingClauseNode getHavingClauseNode() {
        return havingClauseNode;
    }

    public void setHavingClauseNode(HavingClauseNode havingClauseNode) {
        this.havingClauseNode = havingClauseNode;
        addNodeInternal(havingClauseNode);
    }

    public OrderByClauseNode getOrderByClauseNode() {
        return orderByClauseNode;
    }

    public void setOrderByClauseNode(OrderByClauseNode orderByClauseNode) {
        this.orderByClauseNode = orderByClauseNode;
        addNodeInternal(orderByClauseNode);
    }

    public ForUpdateClauseNode getForUpdateClauseNode() {
        return forUpdateClauseNode;
    }

    public void setForUpdateClauseNode(ForUpdateClauseNode forUpdateClauseNode) {
        this.forUpdateClauseNode = forUpdateClauseNode;
        addNodeInternal(forUpdateClauseNode);
    }

    @Override
    public void addNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    protected void addNodeInternal(SqlNode child) {
        if (child != null) {
            super.addNode(child);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public SqlNode copy() {
        CopyingVisitor visitor = new CopyingVisitor();
        SelectStatementNode clone = new SelectStatementNode();
        for (SqlNode child : children) {
            child.accept(visitor, clone);
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof SelectStatementNodeVisitor<?, ?>) {
            SelectStatementNodeVisitor<R, P> v = (SelectStatementNodeVisitor<R, P>) visitor;
            return v.visitSelectStatementNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(getClass().getSimpleName());
        buf.append(" ");
        for (SqlNode child : children) {
            buf.append(child);
        }
        buf.append("]");
        return buf.toString();
    }

    protected static class CopyingVisitor implements
            SelectClauseNodeVisitor<Void, SelectStatementNode>,
            FromClauseNodeVisitor<Void, SelectStatementNode>,
            WhereClauseNodeVisitor<Void, SelectStatementNode>,
            GroupByClauseNodeVisitor<Void, SelectStatementNode>,
            HavingClauseNodeVisitor<Void, SelectStatementNode>,
            OrderByClauseNodeVisitor<Void, SelectStatementNode>,
            ForUpdateClauseNodeVisitor<Void, SelectStatementNode> {

        @Override
        public Void visitSelectClauseNode(SelectClauseNode node,
                SelectStatementNode p) {
            p.setSelectClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitFromClauseNode(FromClauseNode node,
                SelectStatementNode p) {
            p.setFromClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitWhereClauseNode(WhereClauseNode node,
                SelectStatementNode p) {
            p.setWhereClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitGroupByClauseNode(GroupByClauseNode node,
                SelectStatementNode p) {
            p.setGroupByClauseNode(node);
            return null;
        }

        @Override
        public Void visitHavingClauseNode(HavingClauseNode node,
                SelectStatementNode p) {
            p.setHavingClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitOrderByClauseNode(OrderByClauseNode node,
                SelectStatementNode p) {
            p.setOrderByClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitForUpdateClauseNode(ForUpdateClauseNode node,
                SelectStatementNode p) {
            p.setForUpdateClauseNode(node.copy());
            return null;
        }

        @Override
        public Void visitUnknownNode(SqlNode node, SelectStatementNode p) {
            assertUnreachable();
            return null;
        }

    }
}
