package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

/** @author bakenezumi */
public class SqlBatchDeleteQuery extends SqlBatchModifyQuery implements BatchDeleteQuery {

  public SqlBatchDeleteQuery() {
    super(SqlKind.DELETE);
  }
}
