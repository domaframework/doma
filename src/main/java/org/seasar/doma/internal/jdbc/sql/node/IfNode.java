package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class IfNode extends AbstractSqlNode implements SpaceStrippingNode {

  protected final SqlLocation location;

  protected final String expression;

  protected final String text;

  public IfNode(SqlLocation location, String expression, String text) {
    assertNotNull(location, expression, text);
    this.location = location;
    this.expression = expression;
    this.text = text;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public String getExpression() {
    return expression;
  }

  @Override
  public void clearChildren() {
    children.clear();
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitIfNode(this, p);
  }
}
