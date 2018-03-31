package org.seasar.doma.jdbc.id;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import org.seasar.doma.GenerationType;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.StringWrapper;

/** The built-in implementation for {@link TableIdGenerator}. */
public class BuiltinTableIdGenerator extends AbstractPreGenerateIdGenerator
    implements TableIdGenerator {

  protected String qualifiedTableName;

  protected String pkColumnName;

  protected String pkColumnValue;

  protected String valueColumnName;

  protected PreparedSql updateSql;

  protected PreparedSql selectSql;

  @Override
  public void setQualifiedTableName(String qualifiedTableName) {
    this.qualifiedTableName = qualifiedTableName;
  }

  @Override
  public void setPkColumnName(String pkColumnName) {
    this.pkColumnName = pkColumnName;
  }

  @Override
  public void setPkColumnValue(String pkColumnValue) {
    this.pkColumnValue = pkColumnValue;
  }

  @Override
  public void setValueColumnName(String valueColumnName) {
    this.valueColumnName = valueColumnName;
  }

  @Override
  public void initialize() {
    if (qualifiedTableName == null) {
      throw new JdbcException(Message.DOMA2033, "qualifiedTableName");
    }
    if (pkColumnName == null) {
      throw new JdbcException(Message.DOMA2033, "pkColumnName");
    }
    if (pkColumnValue == null) {
      throw new JdbcException(Message.DOMA2033, "pkColumnValue");
    }
    if (valueColumnName == null) {
      throw new JdbcException(Message.DOMA2033, "valueColumnName");
    }
    updateSql =
        new PreparedSql(
            SqlKind.UPDATE,
            createUpdateRawSql(),
            createUpdateFormattedSql(),
            null,
            Arrays.asList(
                new ScalarInParameter<>(
                    () -> new BasicScalar<>(new LongWrapper(), true), allocationSize),
                new ScalarInParameter<>(
                    () -> new BasicScalar<>(new StringWrapper(), false), pkColumnValue)),
            SqlLogType.FORMATTED);
    selectSql =
        new PreparedSql(
            SqlKind.SELECT,
            createSelectRawSql(),
            createSelectFormattedSql(),
            null,
            Collections.singletonList(
                new ScalarInParameter<>(
                    () -> new BasicScalar<>(new StringWrapper(), false), pkColumnValue)),
            SqlLogType.FORMATTED);
  }

  protected String createUpdateRawSql() {
    var buf = new StringBuilder(100);
    buf.append("update ");
    buf.append(qualifiedTableName);
    buf.append(" set ");
    buf.append(valueColumnName);
    buf.append(" = ");
    buf.append(valueColumnName);
    buf.append(" + ? where ");
    buf.append(pkColumnName);
    buf.append(" = ?");
    return buf.toString();
  }

  protected String createUpdateFormattedSql() {
    var buf = new StringBuilder(100);
    buf.append("update ");
    buf.append(qualifiedTableName);
    buf.append(" set ");
    buf.append(valueColumnName);
    buf.append(" = ");
    buf.append(valueColumnName);
    buf.append(" + ");
    buf.append(allocationSize);
    buf.append(" where ");
    buf.append(pkColumnName);
    buf.append(" = '");
    buf.append(pkColumnValue);
    buf.append("'");
    return buf.toString();
  }

  protected String createSelectRawSql() {
    var buf = new StringBuilder(100);
    buf.append("select ");
    buf.append(valueColumnName);
    buf.append(" from ");
    buf.append(qualifiedTableName);
    buf.append(" where ");
    buf.append(pkColumnName);
    buf.append(" = ?");
    return new String(buf);
  }

  protected String createSelectFormattedSql() {
    var buf = new StringBuilder(100);
    buf.append("select ");
    buf.append(valueColumnName);
    buf.append(" from ");
    buf.append(qualifiedTableName);
    buf.append(" where ");
    buf.append(pkColumnName);
    buf.append(" = '");
    buf.append(pkColumnValue);
    buf.append("'");
    return new String(buf);
  }

  @Override
  protected long getNewInitialValue(final IdGenerationConfig config) {
    var controller = config.getRequiresNewController();
    try {
      var value =
          controller.requiresNew(
              () -> {
                updateId(config, updateSql);
                return selectId(config, selectSql);
              });
      return value - allocationSize;
    } catch (Throwable t) {
      throw new JdbcException(Message.DOMA2018, t, config.getEntityDesc().getName(), t);
    }
  }

  protected void updateId(IdGenerationConfig config, PreparedSql sql) {
    var logger = config.getJdbcLogger();
    var connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      var preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "updateId", sql);
        setupOptions(config, preparedStatement);
        preparedStatement.setLong(1, allocationSize);
        preparedStatement.setString(2, pkColumnValue);
        var rows = preparedStatement.executeUpdate();
        if (rows != 1) {
          throw new JdbcException(Message.DOMA2017, config.getEntityDesc().getName());
        }
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2018, e, config.getEntityDesc().getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
  }

  protected long selectId(IdGenerationConfig config, PreparedSql sql) {
    var logger = config.getJdbcLogger();
    var connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      var preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "selectId", sql);
        setupOptions(config, preparedStatement);
        preparedStatement.setString(1, pkColumnValue);
        var resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
          var result = resultSet.getObject(1);
          if (result instanceof Number) {
            return ((Number) result).longValue();
          }
        }
        throw new JdbcException(Message.DOMA2017, config.getEntityDesc().getName());
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2018, e, config.getEntityDesc().getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
  }

  @Override
  public GenerationType getGenerationType() {
    return GenerationType.TABLE;
  }
}
