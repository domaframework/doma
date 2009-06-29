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
