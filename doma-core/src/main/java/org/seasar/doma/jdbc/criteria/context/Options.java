package org.seasar.doma.jdbc.criteria.context;

import org.seasar.doma.jdbc.SqlLogType;

public class Options {
  private String comment;
  private SqlLogType sqlLogType = SqlLogType.FORMATTED;
  private int queryTimeout = 0;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  public int getQueryTimeout() {
    return queryTimeout;
  }

  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }
}
