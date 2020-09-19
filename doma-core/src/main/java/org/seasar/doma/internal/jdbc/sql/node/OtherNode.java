package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class OtherNode extends AbstractSqlNode {

  protected static final Map<String, OtherNode> otherNodeMap = new HashMap<>();

  static {
    otherNodeMap.put(",", new OtherNode(","));
    otherNodeMap.put("=", new OtherNode("="));
    otherNodeMap.put(">", new OtherNode(">"));
    otherNodeMap.put("<", new OtherNode("<"));
    otherNodeMap.put("-", new OtherNode("-"));
    otherNodeMap.put("+", new OtherNode("+"));
    otherNodeMap.put("*", new OtherNode("*"));
    otherNodeMap.put("/", new OtherNode("/"));
    otherNodeMap.put("(", new OtherNode("("));
    otherNodeMap.put(")", new OtherNode(")"));
    otherNodeMap.put(";", new OtherNode(";"));
  }

  protected final String other;

  private OtherNode(String other) {
    assertNotNull(other);
    this.other = other;
  }

  public String getOther() {
    return other;
  }

  @Override
  public void appendNode(SqlNode child) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitOtherNode(this, p);
  }

  public static OtherNode of(String other) {
    OtherNode otherNode = otherNodeMap.get(other);
    if (otherNode != null) {
      return otherNode;
    }
    return new OtherNode(other);
  }
}
