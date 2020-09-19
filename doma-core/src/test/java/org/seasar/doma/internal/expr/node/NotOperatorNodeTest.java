package org.seasar.doma.internal.expr.node;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

public class NotOperatorNodeTest {

  protected final ExpressionLocation location = new ExpressionLocation("", 0);

  protected final LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected final LiteralNode falseLiteral =
      new LiteralNode(location, "false", false, boolean.class);

  protected final LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

  @Test
  public void test_true() {
    NotOperatorNode node = new NotOperatorNode(location, "!");
    node.setNode(trueLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_false() {
    NotOperatorNode node = new NotOperatorNode(location, "!");
    node.setNode(falseLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void test_null() {
    NotOperatorNode node = new NotOperatorNode(location, "!");
    node.setNode(nullLiteral);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }
}
