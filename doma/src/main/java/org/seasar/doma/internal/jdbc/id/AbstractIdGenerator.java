package org.seasar.doma.internal.jdbc.id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.command.Jdbcs;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public abstract class AbstractIdGenerator implements IdGenerator {

    protected long getGeneratedValue(IdGenerationConfig config, Sql<?> sql) {
        JdbcLogger logger = config.getJdbcLogger();
        Connection connection = Jdbcs.getConnection(config.getDataSource());
        try {
            PreparedStatement preparedStatement = Jdbcs
                    .prepareStatement(connection, sql.getRawSql());
            try {
                logger.logSql(getClass().getName(), "getGeneratedId", sql);
                setupOptions(config, preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                return getGeneratedValue(config, resultSet);
            } catch (SQLException e) {
                throw new JdbcException(MessageCode.DOMA2018, e, config
                        .getEntity().__getName(), e);
            } finally {
                Jdbcs.close(preparedStatement, logger);
            }
        } finally {
            Jdbcs.close(connection, logger);
        }
    }

    protected void setupOptions(IdGenerationConfig config,
            PreparedStatement preparedStatement) throws SQLException {
        if (config.getFetchSize() > 0) {
            preparedStatement.setFetchSize(config.getFetchSize());
        }
        if (config.getMaxRows() > 0) {
            preparedStatement.setMaxRows(config.getMaxRows());
        }
        if (config.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(config.getQueryTimeout());
        }
    }

    protected long getGeneratedValue(IdGenerationConfig context,
            ResultSet resultSet) throws SQLException {
        JdbcLogger logger = context.getJdbcLogger();
        try {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new JdbcException(MessageCode.DOMA2017, context.getEntity()
                    .__getName());
        } catch (final SQLException e) {
            throw new JdbcException(MessageCode.DOMA2018, e, context
                    .getEntity().__getName(), e);
        } finally {
            Jdbcs.close(resultSet, logger);
        }
    }
}
