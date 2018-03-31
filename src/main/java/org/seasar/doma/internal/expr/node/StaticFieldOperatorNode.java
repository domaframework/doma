package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author taedium */
public class StaticFieldOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String className;

  protected final String fieldName;

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  public StaticFieldOperatorNode(
      ExpressionLocation location, String expression, String className, String fieldName) {
    assertNotNull(location, fieldName);
    this.location = location;
    this.expression = expression;
    this.className = className;
    this.fieldName = fieldName;
  }

  public String getClassName() {
    return className;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitStaticFieldOperatorNode(this, p);
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
    return expression;
  }
}
