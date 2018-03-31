package org.seasar.doma.internal.expr.node;

import junit.framework.TestCase;
import org.seasar.doma.internal.expr.ExpressionEvaluator;

public class AndOperatorNodeTest extends TestCase {

  protected ExpressionLocation location = new ExpressionLocation("", 0);

  protected LiteralNode trueLiteral = new LiteralNode(location, "true", true, boolean.class);

  protected LiteralNode falseLiteral = new LiteralNode(location, "false", false, boolean.class);

  protected LiteralNode nullLiteral = new LiteralNode(location, "null", null, Object.class);

  public void test_true_true() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(trueLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void test_true_false() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(falseLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void test_false_true() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(falseLiteral);
    node.setRightNode(trueLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void test_false_false() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(falseLiteral);
    node.setRightNode(falseLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void test_null_null() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(nullLiteral);
    node.setRightNode(nullLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void test_null_notNull() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(nullLiteral);
    node.setRightNode(trueLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void test_notNull_null() throws Exception {
    var node = new AndOperatorNode(location, "&&");
    node.setLeftNode(trueLiteral);
    node.setRightNode(nullLiteral);
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(node);
    assertFalse(evaluationResult.getBooleanValue());
  }
}
