package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

public interface AppendableSqlNode extends SqlNode {

  void appendNode(SqlNode child);
}
