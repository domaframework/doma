package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author taedium */
public class FieldOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String fieldName;

  protected ExpressionNode targetObjectNode;

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  public FieldOperatorNode(ExpressionLocation location, String expression, String fieldName) {
    assertNotNull(location, fieldName);
    this.location = location;
    this.expression = expression;
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public ExpressionNode getTargetObjectNode() {
    return targetObjectNode;
  }

  public void setTargetObjectNode(ExpressionNode targetObjectNode) {
    this.targetObjectNode = targetObjectNode;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitFieldOperatorNode(this, p);
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
    return targetObjectNode + expression;
  }
}
