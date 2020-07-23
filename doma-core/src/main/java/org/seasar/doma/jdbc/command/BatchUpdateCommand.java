package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.BatchUpdateQuery;

public class BatchUpdateCommand extends BatchModifyCommand<BatchUpdateQuery> {

  public BatchUpdateCommand(BatchUpdateQuery query) {
    super(query);
  }

  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    int[] rows = executeBatch(preparedStatement, sqls);
    query.incrementVersions();
    return rows;
  }
}
