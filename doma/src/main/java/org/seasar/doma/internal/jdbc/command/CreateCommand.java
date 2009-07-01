package org.seasar.doma.internal.jdbc.command;

import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.CreateQuery;

/**
 * @author taedium
 * 
 */
public class CreateCommand<R> implements Command<R, CreateQuery<R>> {

    protected final CreateQuery<R> query;

    public CreateCommand(CreateQuery<R> query) {
        this.query = query;
    }

    @Override
    public R execute() {
        Connection connection = Jdbcs.getConnection(query.getConfig()
                .dataSource());
        try {
            return query.create(connection);
        } catch (SQLException e) {
            // TODO
            throw new RuntimeException(e);
        } finally {
            Jdbcs.close(connection, query.getConfig().jdbcLogger());
        }
    }

}
