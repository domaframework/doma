package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.Message;

public class EmptyWhereClauseException extends JdbcException {

  private final Sql<?> sql;

  public EmptyWhereClauseException(Sql<?> sql) {
    super(Message.DOMA6006, choiceSql(sql.getSqlLogType(), sql.getRawSql(), sql.getFormattedSql()));
    this.sql = sql;
  }

  public Sql<?> getSql() {
    return sql;
  }
}
