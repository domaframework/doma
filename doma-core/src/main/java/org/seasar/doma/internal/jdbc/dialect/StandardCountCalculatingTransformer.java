package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.jdbc.SqlNode;

public class StandardCountCalculatingTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  protected boolean processed;

  public SqlNode transform(SqlNode sqlNode) {
    AnonymousNode result = new AnonymousNode();
    for (SqlNode child : sqlNode.getChildren()) {
      result.appendNode(child.accept(this, null));
    }
    return result;
  }

  @Override
  protected SqlNode defaultAction(SqlNode node, Void p) {
    return node;
  }
}
