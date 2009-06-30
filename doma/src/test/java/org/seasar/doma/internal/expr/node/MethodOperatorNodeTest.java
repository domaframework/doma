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

import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class MethodOperatorNodeTest extends TestCase {

    protected ExpressionLocation location = new ExpressionLocation("", 0);

    public void testOneParameter() throws Exception {
        MethodOperatorNode node = new MethodOperatorNode(location, ".eq", "eq");
        node.setTargetObjectNode(new VariableNode(location, "hoge"));
        CommaOperatorNode parametersNode = new CommaOperatorNode(location, ",");
        parametersNode.addNode(new LiteralNode(location, "\"aaa\"", "aaa",
                String.class));
        node.setParametersNode(parametersNode);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        evaluator.add("hoge", new StringDomain("aaa"));
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertTrue(evaluationResult.getBooleanValue());
    }

}
