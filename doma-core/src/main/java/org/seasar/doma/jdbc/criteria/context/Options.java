package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.SqlLogType;

public class Options {

  private String comment;
  private SqlLogType sqlLogType = SqlLogType.FORMATTED;

  public String comment() {
    return comment;
  }

  public void comment(String comment) {
    this.comment = comment;
  }

  public SqlLogType sqlLogType() {
    return sqlLogType;
  }

  public void sqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = Objects.requireNonNull(sqlLogType);
  }
}
