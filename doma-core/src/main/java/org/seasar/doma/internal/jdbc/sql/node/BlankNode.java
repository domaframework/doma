package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.jdbc.SqlNode;

public interface BlankNode extends SqlNode {

  String getBlank();

  boolean isEol();
}
