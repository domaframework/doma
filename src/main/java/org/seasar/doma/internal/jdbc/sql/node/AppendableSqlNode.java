package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

public interface AppendableSqlNode extends SqlNode {

  /**
   * 子ノードを追加します。
   *
   * @param child 子ノード
   */
  void appendNode(SqlNode child);
}
