package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class OrderByClauseNode extends AbstractClauseNode implements RemovableClauseNode {

  public OrderByClauseNode(String word) {
    super(word);
  }

  public OrderByClauseNode(WordNode wordNode) {
    super(wordNode);
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitOrderByClauseNode(this, p);
  }
}
