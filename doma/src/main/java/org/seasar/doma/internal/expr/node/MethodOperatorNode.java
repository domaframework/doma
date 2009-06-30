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
package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class MethodOperatorNode implements OperatorNode {

    protected static final int PRIORITY = 0;

    protected final ExpressionLocation location;

    protected final String operator;

    protected final String name;

    protected ExpressionNode targetObjectNode;

    protected ExpressionNode parametersNode;

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    public MethodOperatorNode(ExpressionLocation location, String operator,
            String name) {
        assertNotNull(location, name);
        this.location = location;
        this.operator = operator;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getTargetObjectNode() {
        return targetObjectNode;
    }

    public void setTargetObjectNode(ExpressionNode targetObjectNode) {
        this.targetObjectNode = targetObjectNode;
    }

    public ExpressionNode getParametersNode() {
        return parametersNode;
    }

    public void setParametersNode(ExpressionNode parametersNode) {
        this.parametersNode = parametersNode;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitMethodOperatorNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return targetObjectNode + operator + parametersNode;
    }

}
