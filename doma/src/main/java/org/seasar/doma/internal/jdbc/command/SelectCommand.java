package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class SelectCommand<R> implements Command<R, SelectQuery> {

    protected final SelectQuery query;

    protected final PreparedSql sql;

    protected final ResultSetHandler<R> resultSetHandler;

    public SelectCommand(SelectQuery query, ResultSetHandler<R> resultSetHandler) {
        assertNotNull(query, resultSetHandler);
        this.query = query;
        this.sql = query.getSql();
        this.resultSetHandler = resultSetHandler;
    }

    public R execute() {
        PreparedSql sql = query.getSql();
        Connection connection = Jdbcs.getConnection(query.getConfig()
                .dataSource());
        try {
            PreparedStatement preparedStatement = Jdbcs
                    .prepareStatement(connection, sql.getRawSql());
            try {
                log();
                setupOptions(preparedStatement);
                bindParameters(preparedStatement);
                return executeQuery(preparedStatement);
            } catch (SQLException e) {
                throw new JdbcException(MessageCode.DOMA2009, e, e);
            } finally {
                Jdbcs.close(preparedStatement, query.getConfig().jdbcLogger());
            }
        } finally {
            Jdbcs.close(connection, query.getConfig().jdbcLogger());
        }
    }

    protected void log() {
        query.getConfig().jdbcLogger().logSql(query.getClassName(), query
                .getMethodName(), sql);
    }

    protected void setupOptions(PreparedStatement preparedStatement)
            throws SQLException {
        if (query.getFetchSize() > 0) {
            preparedStatement.setFetchSize(query.getFetchSize());
        }
        if (query.getMaxRows() > 0) {
            preparedStatement.setMaxRows(query.getMaxRows());
        }
        if (query.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected void bindParameters(PreparedStatement preparedStatement)
            throws SQLException {
        PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(
                query);
        binder.bind(preparedStatement, sql.getParameters());
    }

    protected R executeQuery(PreparedStatement preparedStatement)
            throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            return resultSetHandler.handle(resultSet, query);
        } finally {
            Jdbcs.close(resultSet, query.getConfig().jdbcLogger());
        }
    }

}
