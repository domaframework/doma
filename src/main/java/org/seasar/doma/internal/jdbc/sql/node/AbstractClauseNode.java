package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

public abstract class AbstractClauseNode extends AbstractSqlNode implements ClauseNode {

  protected final WordNode wordNode;

  protected AbstractClauseNode(String word) {
    this(new WordNode(word, true));
  }

  protected AbstractClauseNode(WordNode wordNode) {
    assertNotNull(wordNode);
    this.wordNode = wordNode;
  }

  @Override
  public WordNode getWordNode() {
    return wordNode;
  }
}
