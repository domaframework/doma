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

import junit.framework.TestCase;

import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;

/**
 * @author taedium
 * 
 */
public class MethodOperatorNodeTest extends TestCase {

    protected ExpressionLocation location = new ExpressionLocation("", 0);

    public void testOneParameter() throws Exception {
        MethodOperatorNode node = new MethodOperatorNode(location, ".equals",
                "equals");
        node.setTargetObjectNode(new VariableNode(location, "hoge"));
        CommaOperatorNode commaOperatorNode = new CommaOperatorNode(location,
                ",");
        commaOperatorNode.addNode(new LiteralNode(location, "\"aaa\"", "aaa",
                String.class));
        node.setParametersNode(commaOperatorNode);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("hoge", new Value(String.class, "aaa"));
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertTrue(evaluationResult.getBooleanValue());
    }

}
