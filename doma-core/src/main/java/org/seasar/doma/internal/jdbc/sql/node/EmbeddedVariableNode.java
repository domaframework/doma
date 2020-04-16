package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class EmbeddedVariableNode extends AbstractSqlNode {

  protected final SqlLocation location;

  protected final String variableName;

  protected final String text;

  public EmbeddedVariableNode(SqlLocation location, String variableName, String text) {
    assertNotNull(location, variableName, text);
    this.location = location;
    this.variableName = variableName;
    this.text = text;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public String getVariableName() {
    return variableName;
  }

  public String getText() {
    return text;
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitEmbeddedVariableNode(this, p);
  }
}
