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
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class ParensNode extends AbstractSqlNode {

    protected boolean attachedWithBindVariable;

    protected OtherNode openedParensNode;

    protected OtherNode closedParensNode;

    public ParensNode() {
        openedParensNode = OtherNode.of("(");
    }

    public void setAttachedWithBindVariable(boolean attachedWithBindVariable) {
        this.attachedWithBindVariable = attachedWithBindVariable;
    }

    public boolean isAttachedWithBindVariable() {
        return attachedWithBindVariable;
    }

    public OtherNode getOpenedFragmentNode() {
        return openedParensNode;
    }

    public OtherNode getClosedFragmentNode() {
        return closedParensNode;
    }

    public void close() {
        closedParensNode = OtherNode.of(")");
    }

    @Override
    public ParensNode copy() {
        ParensNode clone = new ParensNode();
        clone.attachedWithBindVariable = attachedWithBindVariable;
        clone.openedParensNode = openedParensNode.copy();
        clone.closedParensNode = closedParensNode.copy();
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
        if (ParensNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ParensNodeVisitor<R, P> v = ParensNodeVisitor.class.cast(visitor);
            return v.visitParensNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(openedParensNode);
        for (SqlNode child : children) {
            buf.append(child);
        }
        buf.append(closedParensNode);
        return buf.toString();
    }
}