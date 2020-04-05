package org.seasar.doma.internal.jdbc.sql.node;

public interface ClauseNode extends AppendableSqlNode {

  WordNode getWordNode();
}
