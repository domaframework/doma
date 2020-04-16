package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class HavingClauseNode extends AbstractClauseNode implements RemovableClauseNode {

  public HavingClauseNode(String word) {
    super(word);
  }

  public HavingClauseNode(WordNode wordNode) {
    super(wordNode);
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitHavingClauseNode(this, p);
  }
}
