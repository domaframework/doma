/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ForBlockNode extends AbstractSqlNode implements BlockNode {

    protected ForNode forNode;

    protected HasNextNode hasNextNode;

    protected EndNode endNode;

    public ForBlockNode() {
    }

    public void setForNode(ForNode forNode) {
        this.forNode = forNode;
        addNodeInternal(forNode);
    }

    public ForNode getForNode() {
        return forNode;
    }

    public void setHasNextNode(HasNextNode hasNextNode) {
        this.hasNextNode = hasNextNode;
        addNodeInternal(hasNextNode);
    }

    public HasNextNode getHasNextNode() {
        return hasNextNode;
    }

    public void setEndNode(EndNode endNode) {
        this.endNode = endNode;
        addNodeInternal(endNode);
    }

    public EndNode getEndNode() {
        return endNode;
    }

    public boolean isHasNextNodeExistent() {
        return hasNextNode != null;
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

    @Override
    public ForBlockNode copy() {
        ForBlockNode clone = new ForBlockNode();
        clone.forNode = forNode.copy();
        if (hasNextNode != null) {
            clone.hasNextNode = hasNextNode.copy();
        }
        clone.endNode = endNode.copy();
        for (SqlNode child : children) {
            clone.addNodeInternal(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (ForBlockNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ForBlockNodeVisitor<R, P> v = ForBlockNodeVisitor.class
                    .cast(visitor);
            return v.visitForBlockNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(forNode);
        if (hasNextNode != null) {
            buf.append(hasNextNode);
        }
        buf.append(endNode);
        return buf.toString();
    }

}
