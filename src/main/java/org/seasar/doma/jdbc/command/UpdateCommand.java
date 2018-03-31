package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.query.UpdateQuery;

/**
 * A command for an SQL UPDATE statement.
 *
 * @see Update
 */
public class UpdateCommand extends ModifyCommand<UpdateQuery> {

  public UpdateCommand(UpdateQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    var rows = executeUpdate(preparedStatement);
    query.incrementVersion();
    return rows;
  }
}
