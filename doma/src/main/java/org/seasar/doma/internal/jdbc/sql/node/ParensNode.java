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
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ParensNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected boolean attachedWithBindVariable;

    protected boolean empty = true;

    protected OtherNode openedParensNode;

    protected OtherNode closedParensNode;

    public ParensNode(SqlLocation location) {
        assertNotNull(location);
        this.location = location;
        openedParensNode = OtherNode.of("(");
    }

    public boolean isAttachedWithBindVariable() {
        return attachedWithBindVariable;
    }

    public void setAttachedWithBindVariable(boolean attachedWithBindVariable) {
        this.attachedWithBindVariable = attachedWithBindVariable;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public OtherNode getOpenedFragmentNode() {
        return openedParensNode;
    }

    public OtherNode getClosedFragmentNode() {
        return closedParensNode;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public void close() {
        closedParensNode = OtherNode.of(")");
    }

    @SuppressWarnings("deprecation")
    @Override
    public ParensNode copy() {
        ParensNode clone = new ParensNode(location);
        clone.attachedWithBindVariable = attachedWithBindVariable;
        if (openedParensNode != null) {
            clone.openedParensNode = openedParensNode.copy();
        }
        if (closedParensNode != null) {
            clone.closedParensNode = closedParensNode.copy();
        }
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof ParensNodeVisitor<?, ?>) {
            ParensNodeVisitor<R, P> v = (ParensNodeVisitor<R, P>) visitor;
            return v.visitParensNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(getClass().getSimpleName());
        buf.append(" ");
        if (openedParensNode != null) {
            buf.append(openedParensNode);
        }
        for (SqlNode child : children) {
            buf.append(child);
        }
        if (closedParensNode != null) {
            buf.append(closedParensNode);
        }
        buf.append("]");
        return buf.toString();
    }
}