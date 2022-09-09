package org.seasar.doma.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.wrapper.Wrapper;

/** Represents a SQL template. */
public class SqlTemplate {
  private final String sql;
  private final Config config;
  private final Map<String, Value> values = new HashMap<>();

  /** @param sql a template. Must not be null. */
  public SqlTemplate(String sql) {
    this(sql, new StandardDialect());
  }

  /**
   * @param sql a template. Must not be null.
   * @param dialect a dialect. Must not be null.
   */
  public SqlTemplate(String sql, Dialect dialect) {
    this(
        sql,
        new Config() {

          @Override
          public DataSource getDataSource() {
            throw new UnsupportedOperationException();
          }

          @Override
          public Dialect getDialect() {
            return dialect;
          }
        });
    Objects.requireNonNull(dialect);
  }

  /**
   * @param sql a template. Must not be null.
   * @param config a configuration. Must not be null.
   */
  public SqlTemplate(String sql, Config config) {
    this.sql = Objects.requireNonNull(sql);
    this.config = Objects.requireNonNull(config);
  }

  /**
   * Adds a value.
   *
   * @param name the value name. Must not be null.
   * @param type the value type. Must not be null.
   * @param value the value. Can be null.
   * @return this instance. Must not be null.
   * @param <T> the value type
   */
  public <T> SqlTemplate add(String name, Class<T> type, T value) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    values.put(name, new Value(type, value));
    return this;
  }

  /**
   * Creates a SQL statement from this template.
   *
   * @return a SQL statement. Must not be null.
   */
  public SqlStatement execute() {
    SqlParser parser = new SqlParser(sql);
    SqlNode node = parser.parse();
    NodePreparedSqlBuilder builder = createNodePreparedSqlBuilder();
    PreparedSql preparedSql = builder.build(node, Function.identity());
    return toSqlStatement(preparedSql);
  }

  private NodePreparedSqlBuilder createNodePreparedSqlBuilder() {
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            values, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    return new NodePreparedSqlBuilder(
        config, SqlKind.SCRIPT, null, evaluator, SqlLogType.FORMATTED);
  }

  private SqlStatement toSqlStatement(PreparedSql preparedSql) {
    List<SqlArgument> arguments =
        preparedSql.getParameters().stream()
            .map(
                it -> {
                  Wrapper<?> w = it.getWrapper();
                  return new SqlArgument(w.getBasicClass(), w.get());
                })
            .collect(Collectors.toList());
    return new SqlStatement(preparedSql.getRawSql(), preparedSql.getFormattedSql(), arguments);
  }
}
