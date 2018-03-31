package org.seasar.doma.jdbc.query;

import org.seasar.doma.FetchType;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlLogType;

/** An object used for building an SQL SELECT statement. */
public interface SelectQuery extends Query {

  @Override
  PreparedSql getSql();

  SelectOptions getOptions();

  boolean isResultEnsured();

  boolean isResultMappingEnsured();

  FetchType getFetchType();

  int getFetchSize();

  int getMaxRows();

  SqlLogType getSqlLogType();

  boolean isResultStream();
}
