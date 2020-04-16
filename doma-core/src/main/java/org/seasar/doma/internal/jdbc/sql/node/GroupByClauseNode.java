package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class GroupByClauseNode extends AbstractClauseNode implements RemovableClauseNode {

  public GroupByClauseNode(String word) {
    super(word);
  }

  public GroupByClauseNode(WordNode wordNode) {
    super(wordNode);
  }

  @Override
  public WordNode getWordNode() {
    return wordNode;
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitGroupByClauseNode(this, p);
  }
}
