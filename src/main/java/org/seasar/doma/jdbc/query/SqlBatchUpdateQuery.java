package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

/** @author bakenezumi */
public class SqlBatchUpdateQuery extends SqlBatchModifyQuery implements BatchUpdateQuery {

  public SqlBatchUpdateQuery() {
    super(SqlKind.UPDATE);
  }

  @Override
  public void incrementVersions() {}
}
