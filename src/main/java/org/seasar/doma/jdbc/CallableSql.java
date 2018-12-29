package org.seasar.doma.jdbc;

import java.util.List;
import java.util.function.Function;

/** @author taedium */
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
