package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author nakamura-to
 * @since 2.3.0
 */
public class PopulateNode extends AbstractSqlNode {

  protected final SqlLocation location;

  protected final String text;

  public PopulateNode(SqlLocation location, String text) {
    assertNotNull(location, text);
    this.location = location;
    this.text = text;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public String getText() {
    return text;
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitPopulateNode(this, p);
  }
}
