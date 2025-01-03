/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.expr.node;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

public class OrOperatorNodeTest {

  protected final ExpressionLocation location = new ExpressionLocation("", 0);

  protected final LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected final LiteralNode falseLiteral =
      new LiteralNode(location, "false", false, boolean.class);

  protected final LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

  @Test
  public void test_true_true() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_true_false() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_true() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(falseLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_false() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(falseLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_null() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(nullLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_true() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(nullLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_true_null() {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }
}
