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
public class ForNode extends AbstractSqlNode implements SpaceStrippingNode {

    protected final SqlLocation location;

    protected final String identifier;

    protected final String expression;

    protected final String text;

    public ForNode(SqlLocation location, String identifier, String expression,
            String text) {
        assertNotNull(location, identifier, expression, text);
        this.location = location;
        this.identifier = identifier;
        this.expression = expression;
        this.text = text;
    }

    public SqlLocation getLocation() {
        return location;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ForNode copy() {
        ForNode clone = new ForNode(location, identifier, expression, text);
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
        if (visitor instanceof ForNodeVisitor<?, ?>) {
            ForNodeVisitor<R, P> v = (ForNodeVisitor<R, P>) visitor;
            return v.visitForNode(this, p);
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
