package org.seasar.doma.internal.jdbc.sql.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNode;

/** @author taedium */
public abstract class AbstractSqlNode implements AppendableSqlNode {

  protected final List<SqlNode> children = new ArrayList<>();

  @Override
  public void appendNode(SqlNode child) {
    if (child == null) {
      throw new DomaNullPointerException("child");
    }
    children.add(child);
  }

  @Override
  public List<SqlNode> getChildren() {
    return Collections.unmodifiableList(children);
  }
}
