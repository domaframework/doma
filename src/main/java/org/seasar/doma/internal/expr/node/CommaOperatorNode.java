package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;

public class CommaOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final List<ExpressionNode> expressionNodes = new ArrayList<ExpressionNode>();

  protected final ExpressionLocation location;

  protected final String expression;

  public CommaOperatorNode(ExpressionLocation location, String expression) {
    assertNotNull(location, expression);
    this.location = location;
    this.expression = expression;
  }

  public void addNode(ExpressionNode expressionNode) {
    expressionNodes.add(expressionNode);
  }

  public List<ExpressionNode> getNodes() {
    return expressionNodes;
  }

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitCommaOperatorNode(this, p);
  }

  @Override
  public ExpressionLocation getLocation() {
    return location;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    for (ExpressionNode node : expressionNodes) {
      buf.append(node.toString());
      buf.append(expression);
      buf.append(" ");
    }
    if (buf.length() > 2) {
      buf.setLength(buf.length() - (expression.length() + 1));
    }
    return buf.toString();
  }
}
