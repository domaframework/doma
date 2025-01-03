package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.UpdateQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

public class UpdateCommand extends ModifyCommand<UpdateQuery> {

  public UpdateCommand(UpdateQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    int rows = statisticManager.executeSql(query.getSql(), () -> executeUpdate(preparedStatement));
    query.incrementVersion();
    return rows;
  }
}
