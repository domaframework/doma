package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class LogicalOperatorNode extends AbstractSqlNode {

  protected final WordNode wordNode;

  public LogicalOperatorNode(String word) {
    this(new WordNode(word, true));
  }

  protected LogicalOperatorNode(WordNode wordNode) {
    this.wordNode = wordNode;
  }

  public WordNode getWordNode() {
    return wordNode;
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLogicalOperatorNode(this, p);
  }
}
