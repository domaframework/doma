package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.BatchDeleteQuery;

public class BatchDeleteCommand extends BatchModifyCommand<BatchDeleteQuery> {

  public BatchDeleteCommand(BatchDeleteQuery query) {
    super(query);
  }

  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    return executeBatch(preparedStatement, sqls);
  }
}
