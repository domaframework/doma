package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.UpsertQuery;

public class UpsertCommand extends ModifyCommand<UpsertQuery> {

  public UpsertCommand(UpsertQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    int rows = executeUpdate(preparedStatement);
    query.generateId(preparedStatement);
    return rows;
  }
}
