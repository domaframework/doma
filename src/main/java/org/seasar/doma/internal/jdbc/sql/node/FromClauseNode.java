package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/** @author taedium */
public class FromClauseNode extends AbstractClauseNode {

  public FromClauseNode(String word) {
    super(word);
  }

  public FromClauseNode(WordNode wordNode) {
    super(wordNode);
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitFromClauseNode(this, p);
  }
}
