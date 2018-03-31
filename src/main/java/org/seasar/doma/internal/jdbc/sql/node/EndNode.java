package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/** @author taedium */
public class EndNode extends AbstractSqlNode implements SpaceStrippingNode {

  protected final String text;

  public EndNode(String text) {
    this.text = text;
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
    return visitor.visitEndNode(this, p);
  }
}
