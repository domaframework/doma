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

/**
 * @author taedium
 * 
 */
public class AndOperatorNodeTest extends TestCase {

    protected ExpressionLocation location = new ExpressionLocation("", 0);

    protected LiteralNode trueLiteral = new LiteralNode(location, "true", true,
            boolean.class);

    protected LiteralNode falseLiteral = new LiteralNode(location, "false",
            false, boolean.class);

    protected LiteralNode nullLiteral = new LiteralNode(location, "null", null,
            Object.class);

    public void test_true_true() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(trueLiteral);
        node.setRightNode(trueLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertTrue(evaluationResult.getBooleanValue());
    }

    public void test_true_false() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(trueLiteral);
        node.setRightNode(falseLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }

    public void test_false_true() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(falseLiteral);
        node.setRightNode(trueLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }

    public void test_false_false() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(falseLiteral);
        node.setRightNode(falseLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }

    public void test_null_null() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(nullLiteral);
        node.setRightNode(nullLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }

    public void test_null_notNull() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(nullLiteral);
        node.setRightNode(trueLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }

    public void test_notNull_null() throws Exception {
        AndOperatorNode node = new AndOperatorNode(location, "&&");
        node.setLeftNode(trueLiteral);
        node.setRightNode(nullLiteral);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(node);
        assertFalse(evaluationResult.getBooleanValue());
    }
}
