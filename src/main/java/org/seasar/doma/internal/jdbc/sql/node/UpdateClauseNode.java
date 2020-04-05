package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author nakamura-to
 * @since 2.3.0
 */
public class UpdateClauseNode extends AbstractClauseNode {

  public UpdateClauseNode(String word) {
    super(word);
  }

  public UpdateClauseNode(WordNode wordNode) {
    super(wordNode);
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitUpdateClauseNode(this, p);
  }
}
