package org.seasar.doma.jdbc;

import java.util.List;

public interface SqlNode {

    void addNode(SqlNode child);

    List<SqlNode> getChildren();

    SqlNode copy();

    <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p);
}
