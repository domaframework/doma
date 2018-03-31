package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.message.Message;

/** An identity provider that reserves identity values in advance. */
public class ReservedIdProvider implements IdProvider {

  protected final Config config;

  protected final EntityDesc<?> entityDesc;

  protected final int reservationSize;

  protected final boolean available;

  protected long[] identities;

  protected int index = 0;

  public ReservedIdProvider(Config config, EntityDesc<?> entityDesc, int reservationSize) {
    assertNotNull(config, entityDesc);
    this.config = config;
    this.entityDesc = entityDesc;
    this.reservationSize = reservationSize;
    this.available = config.getDialect().supportsIdentityReservation();
  }

  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public long get() {
    if (!available) {
      throw new UnsupportedOperationException();
    }
    if (identities == null) {
      identities = getIdentities();
    }
    if (identities.length <= index) {
      throw new IllegalStateException(
          String.format("identities.length=%d, index=%d", identities.length, index));
    }
    return identities[index++];
  }

  protected long[] getIdentities() {
    var identities = new long[reservationSize];
    var sql = createSql();
    var logger = config.getJdbcLogger();
    var connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      var preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "getIdentities", sql);
        setupOptions(preparedStatement);
        var resultSet = preparedStatement.executeQuery();
        try {
          for (var i = 0; i < reservationSize && resultSet.next(); i++) {
            identities[i] = resultSet.getLong(1);
          }
        } catch (final SQLException e) {
          throw new JdbcException(Message.DOMA2083, e, entityDesc.getName(), e);
        } finally {
          JdbcUtil.close(resultSet, logger);
        }
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2083, e, entityDesc.getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
    return identities;
  }

  protected Sql<?> createSql() {
    var naming = config.getNaming();
    var dialect = config.getDialect();
    var catalogName = entityDesc.getCatalogName();
    var schemaName = entityDesc.getSchemaName();
    var tableName = entityDesc.getTableName(naming::apply);
    var idColumnName = entityDesc.getGeneratedIdPropertyDesc().getColumnName(naming::apply);
    var isQuoteRequired = entityDesc.isQuoteRequired();
    var isIdColumnQuoteRequired = entityDesc.getGeneratedIdPropertyDesc().isQuoteRequired();
    return dialect.getIdentityReservationSql(
        catalogName,
        schemaName,
        tableName,
        idColumnName,
        isQuoteRequired,
        isIdColumnQuoteRequired,
        reservationSize);
  }

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
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
}
