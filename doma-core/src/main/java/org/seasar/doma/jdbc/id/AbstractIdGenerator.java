package org.seasar.doma.jdbc.id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.message.Message;

/** A skeletal implementation of the {@link IdGenerator} interface. */
public abstract class AbstractIdGenerator implements IdGenerator {

  /**
   * Executes the SQL and gets a generated identity.
   *
   * @param config the configuration for identity generation
   * @param sql the SQL to get the generated identityL
   * @return the generated identity
   * @throws JdbcException if a JDBC related error occurs
   */
  protected long getGeneratedValue(IdGenerationConfig config, Sql<?> sql) {
    JdbcLogger logger = config.getJdbcLogger();
    StatisticManager statisticManager = config.statisticManager();
    Connection connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      PreparedStatement preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "getGeneratedId", sql);
        setupOptions(config, preparedStatement);
        return statisticManager.executeSql(
            sql,
            () -> {
              ResultSet resultSet = preparedStatement.executeQuery();
              return getGeneratedValue(config, resultSet);
            });
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2018, e, config.getEntityType().getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
  }

  /**
   * Set up options for the {@code preparedStatement} object.
   *
   * @param config the configuration for identity generation
   * @param preparedStatement the prepared statement
   * @throws SQLException if operations for the {@code preparedStatement} are failed
   */
  protected void setupOptions(IdGenerationConfig config, PreparedStatement preparedStatement)
      throws SQLException {
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

  /**
   * Retrieves a generated identity from the {@link ResultSet} object.
   *
   * @param config the configuration for identity generation
   * @param resultSet the result set
   * @return the generated identity
   * @throws JdbcException if a JDBC related error occurs
   */
  protected long getGeneratedValue(IdGenerationConfig config, ResultSet resultSet) {
    JdbcLogger logger = config.getJdbcLogger();
    try {
      if (resultSet.next()) {
        return resultSet.getLong(1);
      }
      throw new JdbcException(Message.DOMA2017, config.getEntityType().getName());
    } catch (final SQLException e) {
      throw new JdbcException(Message.DOMA2018, e, config.getEntityType().getName(), e);
    } finally {
      JdbcUtil.close(resultSet, logger);
    }
  }
}
