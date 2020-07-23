package org.seasar.doma.internal.expr.node;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

public class OrOperatorNodeTest {

  protected ExpressionLocation location = new ExpressionLocation("", 0);

  protected LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected LiteralNode falseLiteral = new LiteralNode(location, "false", false, boolean.class);

  protected LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

  @Test
  public void test_true_true() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_true_false() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_true() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(falseLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_false() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(falseLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_null() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(nullLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_true() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(nullLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_true_null() throws Exception {
    OrOperatorNode node = new OrOperatorNode(location, "||");
    node.setLeftNode(trueLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }
}
