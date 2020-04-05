package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class ForNode extends AbstractSqlNode implements SpaceStrippingNode {

  protected final SqlLocation location;

  protected final String identifier;

  protected final String expression;

  protected final String text;

  public ForNode(SqlLocation location, String identifier, String expression, String text) {
    assertNotNull(location, identifier, expression, text);
    this.location = location;
    this.identifier = identifier;
    this.expression = expression;
    this.text = text;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getExpression() {
    return expression;
  }

  public String getText() {
    return text;
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
    return visitor.visitForNode(this, p);
  }
}
