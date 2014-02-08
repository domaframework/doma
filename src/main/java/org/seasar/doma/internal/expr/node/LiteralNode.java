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
package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

/**
 * @author taedium
 * 
 */
public class LiteralNode implements ExpressionNode {

    protected final ExpressionLocation location;

    protected final String expression;

    protected final Object value;

    protected final Class<?> valueClass;

    public <T> LiteralNode(ExpressionLocation location, String expression,
            T value, Class<T> valueClass) {
        assertNotNull(location, expression, valueClass);
        this.location = location;
        this.expression = expression;
        this.value = value;
        this.valueClass = valueClass;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitLiteralNode(this, p);
    }

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
