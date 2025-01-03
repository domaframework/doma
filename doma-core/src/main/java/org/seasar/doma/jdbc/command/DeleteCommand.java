package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.DeleteQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

public class DeleteCommand extends ModifyCommand<DeleteQuery> {

  public DeleteCommand(DeleteQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    return statisticManager.executeSql(query.getSql(), () -> executeUpdate(preparedStatement));
  }
}
