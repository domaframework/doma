package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

public class SqlBatchUpdateQuery extends SqlBatchModifyQuery implements BatchUpdateQuery {

  public SqlBatchUpdateQuery() {
    super(SqlKind.UPDATE);
  }

  @Override
  public void incrementVersions() {}
}
