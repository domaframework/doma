package org.seasar.doma.internal.expr.node;

import junit.framework.TestCase;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

/** @author taedium */
public class AndOperatorNodeTest extends TestCase {

  protected ExpressionLocation location = new ExpressionLocation("", 0);

  protected LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected LiteralNode falseLiteral = new LiteralNode(location, "false", false, boolean.class);

  protected LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

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
