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

public class BindVariableNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String variableName;

    protected final String text;

    protected WordNode wordNode;

    protected ParensNode parensNode;

    public BindVariableNode(SqlLocation location, String variableName,
            String text) {
        assertNotNull(location, variableName, text);
        this.location = location;
        this.variableName = variableName;
        this.text = text;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BindVariableNode copy() {
        BindVariableNode clone = new BindVariableNode(location, variableName,
                text);
        if (wordNode != null) {
            clone.wordNode = wordNode.copy();
        }
        if (parensNode != null) {
            clone.parensNode = parensNode.copy();
        }
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "addNode");
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof BindVariableNodeVisitor<?, ?>) {
            BindVariableNodeVisitor<R, P> v = (BindVariableNodeVisitor<R, P>) visitor;
            return v.visitBindVariableNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    public WordNode getWordNode() {
        return wordNode;
    }

    public void setWordNode(WordNode wordNode) {
        this.wordNode = wordNode;
    }

    public ParensNode getParensNode() {
        return parensNode;
    }

    public void setParensNode(ParensNode parensNode) {
        this.parensNode = parensNode;
    }

    public boolean isWordNodeIgnored() {
        return wordNode != null;
    }

    public boolean isParensNodeIgnored() {
        return parensNode != null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(getClass().getSimpleName());
        buf.append(" ");
        buf.append(text);
        for (SqlNode child : children) {
            buf.append(child);
        }
        if (wordNode != null) {
            buf.append(wordNode);
        } else if (parensNode != null) {
            buf.append(parensNode);
        }
        buf.append("]");
        return buf.toString();
    }

}
