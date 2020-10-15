package org.seasar.doma.jdbc;

public enum LogKind {
  DAO,
  SKIP,
  SQL,
  LOCAL_TRANSACTION,
  FAILURE;

  private final String fullName;

  LogKind() {
    this.fullName = getClass().getName() + "." + name();
  }

  public String fullName() {
    return fullName;
  }
}
