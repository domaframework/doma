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

public abstract class AbstractSelectQuery extends AbstractQuery implements SelectQuery {

  protected final Map<String, Value> parameters = new HashMap<>();

  protected SelectOptions options = SelectOptions.get();

  protected boolean resultEnsured;

  protected boolean resultMappingEnsured;

  protected FetchType fetchType;

  protected int fetchSize;

  protected int maxRows;

  protected EntityType<?> entityType;

  protected PreparedSql sql;

  protected SqlLogType sqlLogType;

  protected boolean resultStream;

  protected AggregateStrategyType aggregateStrategyType;

  protected AbstractSelectQuery() {}

  @Override
  public void prepare() {
    super.prepare();
    prepareOptions();
    prepareSql();
    assertNotNull(sql);
  }

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

  @Override
  public SelectOptions getOptions() {
    return options;
  }

  public void setOptions(SelectOptions options) {
    this.options = options;
  }

  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    parameters.put(name, new Value(type, value));
  }

  public void addParameters(Map<String, Value> parameters) {
    this.parameters.putAll(parameters);
  }

  public void clearParameters() {
    this.parameters.clear();
  }

  @Override
  public boolean isResultEnsured() {
    return resultEnsured;
  }

  public void setResultEnsured(boolean resultEnsured) {
    this.resultEnsured = resultEnsured;
  }

  @Override
  public boolean isResultMappingEnsured() {
    return resultMappingEnsured;
  }

  public void setResultMappingEnsured(boolean resultMappingEnsured) {
    this.resultMappingEnsured = resultMappingEnsured;
  }

  @Override
  public FetchType getFetchType() {
    return fetchType;
  }

  public void setFetchType(FetchType fetchType) {
    this.fetchType = fetchType;
  }

  @Override
  public int getFetchSize() {
    return fetchSize;
  }

  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  @Override
  public int getMaxRows() {
    return maxRows;
  }

  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }

  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  public boolean isResultStream() {
    return resultStream;
  }

  public void setResultStream(boolean resultStream) {
    this.resultStream = resultStream;
  }

  public void setAggregateStrategyType(AggregateStrategyType aggregateStrategyType) {
    this.aggregateStrategyType = aggregateStrategyType;
  }

  public void setEntityType(EntityType<?> entityType) {
    this.entityType = entityType;
  }

  @Override
  public PreparedSql getSql() {
    return sql;
  }

  @Override
  public String toString() {
    return sql != null ? sql.toString() : null;
  }
}
