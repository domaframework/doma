package org.seasar.doma.internal.expr.node;

/** @author taedium */
public class ExpressionLocation {

  protected final String expression;

  protected final int position;

  public ExpressionLocation(String expression, int position) {
    this.expression = expression;
    this.position = position;
  }

  public String getExpression() {
    return expression;
  }

  public int getPosition() {
    return position;
  }

  @Override
  public String toString() {
    return expression + ":" + position;
  }
}
