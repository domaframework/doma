package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.PrimitiveLongWrapper;

public abstract class AbstractSelectQuery extends AbstractQuery implements SelectQuery {

  protected final Map<String, Value> parameters = new HashMap<String, Value>();

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

  protected void buildSql(
      BiFunction<ExpressionEvaluator, Function<ExpandNode, List<String>>, PreparedSql> sqlBuilder) {
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    sql = sqlBuilder.apply(evaluator, this::expandColumns);
  }

  protected List<String> expandColumns(ExpandNode node) {
    if (entityType == null) {
      SqlLocation location = node.getLocation();
      throw new JdbcException(
          Message.DOMA2144, location.getSql(), location.getLineNumber(), location.getPosition());
    }
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    return entityType
        .getEntityPropertyTypes()
        .stream()
        .map(p -> p.getColumnName(naming::apply, dialect::applyQuote))
        .collect(Collectors.toList());
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
        new SelectCommand<Long>(
            query, new BasicSingleResultHandler<Long>(() -> new PrimitiveLongWrapper()));
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
