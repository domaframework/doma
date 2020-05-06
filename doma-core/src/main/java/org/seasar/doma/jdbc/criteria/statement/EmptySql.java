package org.seasar.doma.jdbc.criteria.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;

public class EmptySql implements Sql<InParameter<?>> {

  private static final String MESSAGE = "This SQL is empty because target entities are empty.";
  private final SqlKind sqlKind;

  public EmptySql(SqlKind sqlKind) {
    this.sqlKind = Objects.requireNonNull(sqlKind);
  }

  @Override
  public SqlKind getKind() {
    return sqlKind;
  }

  @Override
  public String getRawSql() {
    return MESSAGE;
  }

  @Override
  public String getFormattedSql() {
    return MESSAGE;
  }

  @Override
  public String getSqlFilePath() {
    return null;
  }

  @Override
  public List<InParameter<?>> getParameters() {
    return Collections.emptyList();
  }

  @Override
  public SqlLogType getSqlLogType() {
    return SqlLogType.FORMATTED;
  }
}
