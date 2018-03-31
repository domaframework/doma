package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.Insert;
import org.seasar.doma.jdbc.query.InsertQuery;

/**
 * A command for an SQL INSERT statement.
 *
 * @see Insert
 */
public class InsertCommand extends ModifyCommand<InsertQuery> {

  public InsertCommand(InsertQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    var rows = executeUpdate(preparedStatement);
    query.generateId(preparedStatement);
    return rows;
  }
}
