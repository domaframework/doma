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
package org.seasar.doma.internal.jdbc.query;

import java.util.Map;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

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

    @Override
    public boolean isResultEnsured() {
        return true;
    }

    @Override
    public boolean isResultMappingEnsured() {
        return false;
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
                SqlKind.SELECT, null, evaluator);
        sql = sqlBuilder.build(transformedSqlNode);
    }

    @Override
    public void complete() {
    }
}
