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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.seasar.doma.FetchType;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class CountQuery implements SelectQuery {

    protected Config config;

    protected Map<String, Value> parameters;

    protected String callerClassName;

    protected String callerMethodName;

    protected SqlNode sqlNode;

    protected PreparedSql sql;

    protected SelectOptions options;

    protected int fetchSize;

    protected int maxRows;

    protected int queryTimeout;

    protected Method method;

    protected EntityType<?> entityType;

    @Override
    public boolean isResultEnsured() {
        return true;
    }

    @Override
    public boolean isResultMappingEnsured() {
        return false;
    }

    @Override
    public FetchType getFetchType() {
        return FetchType.LAZY;
    }

    @Override
    public int getFetchSize() {
        return fetchSize;
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public SelectOptions getOptions() {
        return options;
    }

    @Override
    public PreparedSql getSql() {
        return sql;
    }

    @Override
    public String getClassName() {
        return callerClassName;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public String getMethodName() {
        return callerMethodName;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public void prepare() {
        SqlNode transformedSqlNode = config.getDialect()
                .transformSelectSqlNodeForGettingCount(sqlNode);
        ExpressionEvaluator evaluator = new ExpressionEvaluator(parameters,
                config.getDialect().getExpressionFunctions(),
                config.getClassHelper());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config,
                SqlKind.SELECT, null, evaluator, this::expandColumns);
        sql = sqlBuilder.build(transformedSqlNode);
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

    @Override
    public void complete() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
