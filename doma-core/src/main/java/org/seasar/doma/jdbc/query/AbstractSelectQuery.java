/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.seasar.doma.DomaException;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.aggregate.AggregateStrategyType;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.PrimitiveLongWrapper;

/**
 * The base abstract class for all select query implementations.
 *
 * <p>This class provides common functionality for queries that select data from a database.
 */
public abstract class AbstractSelectQuery extends AbstractQuery implements SelectQuery {

  /** The parameters for the query. */
  protected final Map<String, Value> parameters = new HashMap<>();

  /** The select options. */
  protected SelectOptions options = SelectOptions.get();

  /** Whether the query is expected to return at least one result. */
  protected boolean resultEnsured;

  /** Whether the result mapping is ensured. */
  protected boolean resultMappingEnsured;

  /** The fetch type for this query. */
  protected FetchType fetchType;

  /** The fetch size for this query. */
  protected int fetchSize;

  /** The maximum number of rows to be returned. */
  protected int maxRows;

  /** The entity type for this query. */
  protected EntityType<?> entityType;

  /** The prepared SQL for this query. */
  protected PreparedSql sql;

  /** The SQL log type for this query. */
  protected SqlLogType sqlLogType;

  /** Whether the result should be processed as a stream. */
  protected boolean resultStream;

  /** The aggregate strategy type for this query. */
  protected AggregateStrategyType aggregateStrategyType;

  /** Creates a new instance. */
  protected AbstractSelectQuery() {}

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares options and SQL.
   */
  @Override
  public void prepare() {
    super.prepare();
    prepareOptions();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Prepares the options for this query.
   *
   * <p>This method sets default values for fetch size, max rows, and query timeout if they are not
   * already set.
   */
  protected void prepareOptions() {
    if (fetchSize <= 0) {
      fetchSize = config.getFetchSize();
    }
    if (maxRows <= 0) {
      maxRows = config.getMaxRows();
    }
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
  }

  /**
   * Prepares the SQL for this query.
   *
   * <p>This method must be implemented by subclasses to build the SQL statement.
   */
  protected abstract void prepareSql();

  @Deprecated(forRemoval = true)
  protected void buildSql(
      BiFunction<ExpressionEvaluator, Function<ExpandNode, List<String>>, PreparedSql> sqlBuilder) {
    ExpressionEvaluator evaluator = createExpressionEvaluator();
    sql = sqlBuilder.apply(evaluator, this::expandColumns);
  }

  protected ExpressionEvaluator createExpressionEvaluator() {
    return new ExpressionEvaluator(
        parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
  }

  protected List<String> expandColumns(ExpandNode node) {
    if (entityType == null) {
      SqlLocation location = node.getLocation();
      throw new JdbcException(
          Message.DOMA2144, location.getSql(), location.getLineNumber(), location.getPosition());
    }
    if (aggregateStrategyType != null) {
      // The expandAggregateColumns method does the processing, so nothing is done here.
      return List.of();
    }
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    return entityType.getEntityPropertyTypes().stream()
        .map(p -> p.getColumnName(naming::apply, dialect::applyQuote))
        .collect(Collectors.toList());
  }

  protected List<String> expandAggregateColumns(ExpandNode node, String aliasCsv) {
    if (entityType == null) {
      SqlLocation location = node.getLocation();
      throw new JdbcException(
          Message.DOMA2144, location.getSql(), location.getLineNumber(), location.getPosition());
    }
    if (aggregateStrategyType == null) {
      return List.of();
    }

    Set<String> definedAliases = new HashSet<>(aggregateStrategyType.getTableAliases());
    Set<String> requiredAliases =
        Arrays.stream(aliasCsv.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .peek(
                s -> {
                  if (!definedAliases.contains(s)) {
                    throw new DomaException(Message.DOMA2239, s, aggregateStrategyType.getName());
                  }
                })
            .collect(Collectors.toSet());

    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();

    Stream<Pair<String, EntityType<?>>> rootStream =
        Stream.of(
            new Pair<>(aggregateStrategyType.getTableAlias(), aggregateStrategyType.getRoot()));
    Stream<Pair<String, EntityType<?>>> associationStream =
        aggregateStrategyType.getAssociationLinkerTypes().stream()
            .map(it -> new Pair<>(it.getTableAlias(), it.getTarget()));

    return Stream.concat(rootStream, associationStream)
        .filter(pair -> requiredAliases.isEmpty() || requiredAliases.contains(pair.fst))
        .flatMap(
            pair -> pair.snd.getEntityPropertyTypes().stream().map(it -> new Pair<>(pair.fst, it)))
        .map(
            pair -> {
              String tableAlias = pair.fst;
              String columnName = pair.snd.getColumnName(naming::apply, dialect::applyQuote);
              return String.format("%1$s.%2$s as %1$s_%2$s", tableAlias, columnName);
            })
        .toList();
  }

  protected void populateValues(PopulateNode node, SqlContext context) {
    throw new UnsupportedOperationException();
  }

  protected void executeCount(SqlNode sqlNode) {
    CountQuery query = new CountQuery();
    query.setCallerClassName(callerClassName);
    query.setCallerMethodName(callerMethodName);
    query.setMethod(method);
    query.setConfig(config);
    query.setFetchSize(fetchSize);
    query.setMaxRows(maxRows);
    query.setQueryTimeout(queryTimeout);
    query.setOptions(options);
    query.setSqlNode(sqlNode);
    query.setEntityType(entityType);
    query.setSqlLogType(sqlLogType);
    query.addParameters(parameters);
    query.prepare();
    SelectCommand<Long> command =
        new SelectCommand<>(query, new BasicSingleResultHandler<>(PrimitiveLongWrapper::new));
    long count = command.execute();
    query.complete();
    SelectOptionsAccessor.setCountSize(options, count);
  }

  /** {@inheritDoc} */
  @Override
  public SelectOptions getOptions() {
    return options;
  }

  /**
   * Sets the select options.
   *
   * @param options the select options
   */
  public void setOptions(SelectOptions options) {
    this.options = options;
  }

  /**
   * Adds a parameter to this query.
   *
   * @param name the parameter name
   * @param type the parameter type
   * @param value the parameter value
   */
  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    parameters.put(name, new Value(type, value));
  }

  /**
   * Adds multiple parameters to this query.
   *
   * @param parameters the parameters to add
   */
  public void addParameters(Map<String, Value> parameters) {
    this.parameters.putAll(parameters);
  }

  /** Clears all parameters from this query. */
  public void clearParameters() {
    this.parameters.clear();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isResultEnsured() {
    return resultEnsured;
  }

  /**
   * Sets whether the query is expected to return at least one result.
   *
   * @param resultEnsured {@code true} if the query is expected to return at least one result
   */
  public void setResultEnsured(boolean resultEnsured) {
    this.resultEnsured = resultEnsured;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isResultMappingEnsured() {
    return resultMappingEnsured;
  }

  /**
   * Sets whether the result mapping is ensured.
   *
   * @param resultMappingEnsured {@code true} if the result mapping is ensured
   */
  public void setResultMappingEnsured(boolean resultMappingEnsured) {
    this.resultMappingEnsured = resultMappingEnsured;
  }

  /** {@inheritDoc} */
  @Override
  public FetchType getFetchType() {
    return fetchType;
  }

  /**
   * Sets the fetch type for this query.
   *
   * @param fetchType the fetch type
   */
  public void setFetchType(FetchType fetchType) {
    this.fetchType = fetchType;
  }

  /** {@inheritDoc} */
  @Override
  public int getFetchSize() {
    return fetchSize;
  }

  /**
   * Sets the fetch size for this query.
   *
   * @param fetchSize the fetch size
   */
  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxRows() {
    return maxRows;
  }

  /**
   * Sets the maximum number of rows to be returned.
   *
   * @param maxRows the maximum number of rows
   */
  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }

  /** {@inheritDoc} */
  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  /**
   * Sets the SQL log type for this query.
   *
   * @param sqlLogType the SQL log type
   */
  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isResultStream() {
    return resultStream;
  }

  /**
   * Sets whether the result should be processed as a stream.
   *
   * @param resultStream {@code true} if the result should be processed as a stream
   */
  public void setResultStream(boolean resultStream) {
    this.resultStream = resultStream;
  }

  /**
   * Sets the aggregate strategy type for this query.
   *
   * @param aggregateStrategyType the aggregate strategy type
   */
  public void setAggregateStrategyType(AggregateStrategyType aggregateStrategyType) {
    this.aggregateStrategyType = aggregateStrategyType;
  }

  /**
   * Sets the entity type for this query.
   *
   * @param entityType the entity type
   */
  public void setEntityType(EntityType<?> entityType) {
    this.entityType = entityType;
  }

  /** {@inheritDoc} */
  @Override
  public PreparedSql getSql() {
    return sql;
  }

  @Override
  public String toString() {
    return sql != null ? sql.toString() : null;
  }
}
