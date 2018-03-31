package org.seasar.doma.jdbc.command;

import java.sql.Connection;
import java.sql.SQLException;
import org.seasar.doma.*;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.query.CreateQuery;
import org.seasar.doma.message.Message;

/**
 * A command to create a JDBC object that is mapped to the SQL type.
 *
 * @param <RESULT> the result type
 * @see ArrayFactory
 * @see BlobFactory
 * @see ClobFactory
 * @see NClobFactory
 * @see SQLXMLFactory
 */
public class CreateCommand<RESULT> implements Command<RESULT> {

  protected final CreateQuery<RESULT> query;

  public CreateCommand(CreateQuery<RESULT> query) {
    this.query = query;
  }

  @Override
  public RESULT execute() {
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      return query.create(connection);
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2008, e, e);
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }
}
