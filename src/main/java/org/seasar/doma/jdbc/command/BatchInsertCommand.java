package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.BatchInsertQuery;

/**
 * A command for a batch of SQL INSERT statements.
 *
 * @see BatchInsert
 */
public class BatchInsertCommand extends BatchModifyCommand<BatchInsertQuery> {

  public BatchInsertCommand(BatchInsertQuery query) {
    super(query);
  }

  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    if (query.isBatchSupported()) {
      return executeBatch(preparedStatement, sqls);
    }
    var sqlSize = sqls.size();
    var updatedRows = new int[sqlSize];
    var i = 0;
    for (var sql : sqls) {
      log(sql);
      bindParameters(preparedStatement, sql);
      updatedRows[i] = executeUpdate(preparedStatement, sql);
      query.generateId(preparedStatement, i);
      i++;
    }
    return updatedRows;
  }

  protected int executeUpdate(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    try {
      return preparedStatement.executeUpdate();
    } catch (SQLException e) {
      var dialect = query.getConfig().getDialect();
      if (dialect.isUniqueConstraintViolated(e)) {
        throw new BatchUniqueConstraintException(
            query.getConfig().getExceptionSqlLogType(), sql, e);
      }
      throw e;
    }
  }
}
