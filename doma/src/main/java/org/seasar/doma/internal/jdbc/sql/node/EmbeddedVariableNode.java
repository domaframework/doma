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

public class EmbeddedVariableNode extends AbstractSqlNode {

    protected final SqlLocation location;

    protected final String variableName;

    protected final String text;

    public EmbeddedVariableNode(SqlLocation location, String variableName,
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
    public EmbeddedVariableNode copy() {
        EmbeddedVariableNode clone = new EmbeddedVariableNode(location,
                variableName, text);
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
        if (visitor instanceof EmbeddedVariableNodeVisitor<?, ?>) {
            EmbeddedVariableNodeVisitor<R, P> v = (EmbeddedVariableNodeVisitor<R, P>) visitor;
            return v.visitEmbeddedVariableNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
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
        buf.append("]");
        return buf.toString();
    }

}
