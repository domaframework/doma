package org.seasar.doma.internal.expr.node;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

public class AndOperatorNodeTest {

  protected final ExpressionLocation location = new ExpressionLocation("", 0);

  protected final LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected final LiteralNode falseLiteral =
      new LiteralNode(location, "false", false, boolean.class);

  protected final LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

  @Test
  public void test_true_true() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_true_false() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_true() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(falseLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false_false() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(falseLiteral);
    node.setRightNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_null() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(nullLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null_notNull() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(nullLiteral);
    node.setRightNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_notNull_null() throws Exception {
    AndOperatorNode node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }
}
