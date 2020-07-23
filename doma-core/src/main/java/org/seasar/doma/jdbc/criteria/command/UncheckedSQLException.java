package org.seasar.doma.jdbc.criteria.command;

import java.sql.SQLException;
import java.util.Objects;

class UncheckedSQLException extends RuntimeException {
  public UncheckedSQLException(SQLException cause) {
    super(Objects.requireNonNull(cause));
  }

  @Override
  public SQLException getCause() {
    return (SQLException) super.getCause();
  }
}
