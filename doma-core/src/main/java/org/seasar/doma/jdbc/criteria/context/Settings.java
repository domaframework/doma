package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.SqlLogType;

public class Settings {
  private String comment;
  private SqlLogType sqlLogType = SqlLogType.FORMATTED;
  private int queryTimeout = 0;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    Objects.requireNonNull(comment);
    this.comment = comment;
  }

  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    Objects.requireNonNull(sqlLogType);
    this.sqlLogType = sqlLogType;
  }

  public int getQueryTimeout() {
    return queryTimeout;
  }

  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }
}
