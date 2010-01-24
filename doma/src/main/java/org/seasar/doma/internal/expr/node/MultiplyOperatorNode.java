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

/**
 * @author taedium
 * 
 */
public class MultiplyOperatorNode extends AbstractArithmeticOperatorNode {

    protected static final int PRIORITY = 60;

    public MultiplyOperatorNode(ExpressionLocation location, String operator) {
        super(location, operator, PRIORITY);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitMultiplyOperatorNode(this, p);
    }

}
