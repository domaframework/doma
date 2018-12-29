package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.SqlKind;

/** @author taedium */
public class SqlUpdateQuery extends SqlModifyQuery implements UpdateQuery {

  public SqlUpdateQuery() {
    super(SqlKind.UPDATE);
  }

  @Override
  public void incrementVersion() {}
}
