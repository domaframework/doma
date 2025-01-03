package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.InsertQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

public class InsertCommand extends ModifyCommand<InsertQuery> {

  public InsertCommand(InsertQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    return statisticManager.executeSql(
        query.getSql(),
        () -> {
          int rows = executeUpdate(preparedStatement);
          query.generateId(preparedStatement);
          return rows;
        });
  }
}
