package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class WhitespaceNode extends AbstractSqlNode {

  protected static final Map<String, WhitespaceNode> nodeMap =
      new HashMap<String, WhitespaceNode>();

  static {
    nodeMap.put(String.valueOf('\u0009'), new WhitespaceNode('\u0009'));
    nodeMap.put(String.valueOf('\u000B'), new WhitespaceNode('\u000B'));
    nodeMap.put(String.valueOf('\u000C'), new WhitespaceNode('\u000C'));
    nodeMap.put(String.valueOf('\u001C'), new WhitespaceNode('\u001C'));
    nodeMap.put(String.valueOf('\u001D'), new WhitespaceNode('\u001D'));
    nodeMap.put(String.valueOf('\u001E'), new WhitespaceNode('\u001E'));
    nodeMap.put(String.valueOf('\u001F'), new WhitespaceNode('\u001F'));
    nodeMap.put(String.valueOf('\u0020'), new WhitespaceNode('\u0020'));
  }

  protected final String whitespace;

  private WhitespaceNode(char whitespace) {
    this(String.valueOf(whitespace));
  }

  private WhitespaceNode(String whitespace) {
    assertNotNull(whitespace);
    this.whitespace = whitespace;
  }

  public String getWhitespace() {
    return whitespace;
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
    return visitor.visitWhitespaceNode(this, p);
  }

  public static WhitespaceNode of(String whitespace) {
    WhitespaceNode whitespaceNode = nodeMap.get(whitespace);
    if (whitespaceNode != null) {
      return whitespaceNode;
    }
    return new WhitespaceNode(whitespace);
  }
}
