/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
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
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.LongWrapper;

/**
 * @author nakamura-to
 *
 */
public abstract class AbstractSelectQuery implements SelectQuery {

    protected final Map<String, Value> parameters = new HashMap<String, Value>();

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    protected SelectOptions options = SelectOptions.get();

    protected boolean resultEnsured;

    protected boolean resultMappingEnsured;

    protected FetchType fetchType;

    protected int fetchSize;

    protected int maxRows;

    protected int queryTimeout;

    protected Method method;

    protected EntityType<?> entityType;

    protected PreparedSql sql;

    protected AbstractSelectQuery() {
    }

    @Override
    public void prepare() {
        assertNotNull(config, callerClassName, callerMethodName);
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
        ExpressionEvaluator evaluator = new ExpressionEvaluator(parameters,
                config.getDialect().getExpressionFunctions(),
                config.getClassHelper());
        sql = sqlBuilder.apply(evaluator, this::expandColumns);
    }

    protected List<String> expandColumns(ExpandNode node) {
        if (entityType == null) {
            SqlLocation location = node.getLocation();
            throw new JdbcException(Message.DOMA2144, location.getSql(),
                    location.getLineNumber(), location.getPosition());
        }
        Dialect dialect = config.getDialect();
        return entityType.getEntityPropertyTypes().stream()
                .map(p -> p.getColumnName(dialect::applyQuote))
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
        query.addParameters(parameters);
        query.prepare();
        SelectCommand<Long> command = new SelectCommand<Long>(query,
                new BasicSingleResultHandler<Long>(() -> new LongWrapper(),
                        true));
        long count = command.execute();
        query.complete();
        SelectOptionsAccessor.setCountSize(options, count);
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String getClassName() {
        return callerClassName;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    @Override
    public String getMethodName() {
        return callerMethodName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
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

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
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