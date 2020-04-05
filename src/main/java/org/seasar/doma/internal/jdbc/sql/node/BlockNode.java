package org.seasar.doma.internal.jdbc.sql.node;

public interface BlockNode extends AppendableSqlNode {

  void setEndNode(EndNode endNode);
}
