package org.seasar.doma.jdbc;

import java.sql.CallableStatement;
import java.util.List;
import java.util.function.Function;

/**
 * A callable SQL.
 *
 * <p>This is related to {@link CallableStatement}.
 */
public class CallableSql extends AbstractSql<SqlParameter> {

  public CallableSql(
      SqlKind kind,
      CharSequence rawSql,
      CharSequence formattedSql,
      List<? extends SqlParameter> parameters,
      SqlLogType sqlLogType,
      Function<String, String> converter) {
    super(kind, rawSql, formattedSql, null, parameters, sqlLogType, converter);
  }
}
