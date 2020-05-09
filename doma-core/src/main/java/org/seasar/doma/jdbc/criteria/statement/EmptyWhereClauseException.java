package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.Message;

public class EmptyWhereClauseException extends DomaException {

  private final Sql<?> sql;

  public EmptyWhereClauseException(Sql<?> sql) {
    super(Message.DOMA6006, Objects.requireNonNull(sql).getRawSql());
    this.sql = sql;
  }

  public Sql<?> getSql() {
    return sql;
  }
}
