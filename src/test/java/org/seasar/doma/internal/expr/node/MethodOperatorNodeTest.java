package org.seasar.doma.internal.expr.node;

import junit.framework.TestCase;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;

public class MethodOperatorNodeTest extends TestCase {

  protected ExpressionLocation location = new ExpressionLocation("", 0);

  public void testOneParameter() throws Exception {
    var node = new MethodOperatorNode(location, ".equals", "equals");
    node.setTargetObjectNode(new VariableNode(location, "hoge"));
    var commaOperatorNode = new CommaOperatorNode(location, ",");
    commaOperatorNode.addNode(new LiteralNode(location, "\"aaa\"", "aaa", String.class));
    node.setParametersNode(commaOperatorNode);
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    var evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }
}
