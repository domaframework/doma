package org.seasar.doma.jdbc;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.Function;

/**
 * A prepared SQL.
 *
 * <p>This is related to {@link PreparedStatement}.
 */
public class PreparedSql extends AbstractSql<InParameter<?>> {

  public PreparedSql(
      SqlKind kind,
      CharSequence rawSql,
      CharSequence formattedSql,
      String sqlFilePath,
      List<? extends InParameter<?>> parameters,
      SqlLogType sqlLogType) {
    this(kind, rawSql, formattedSql, sqlFilePath, parameters, sqlLogType, Function.identity());
  }

  public PreparedSql(
      SqlKind kind,
      CharSequence rawSql,
      CharSequence formattedSql,
      String sqlFilePath,
      List<? extends InParameter<?>> parameters,
      SqlLogType sqlLogType,
      Function<String, String> commenter) {
    super(kind, rawSql, formattedSql, sqlFilePath, parameters, sqlLogType, commenter);
  }
}
