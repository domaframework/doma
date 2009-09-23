/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQuery implements SelectQuery {

    protected Config config;

    protected String sqlFilePath;

    protected Map<String, Object> parameters = new HashMap<String, Object>();

    protected String callerClassName;

    protected String callerMethodName;

    protected PreparedSql sql;

    protected SelectOptions options = SelectOptions.get();

    protected int fetchSize;

    protected int maxRows;

    protected int queryTimeout;

    public void prepare() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareOptions() {
        if (fetchSize <= 0) {
            fetchSize = config.fetchSize();
        }
        if (maxRows <= 0) {
            maxRows = config.maxRows();
        }
        if (queryTimeout <= 0) {
            queryTimeout = config.queryTimeout();
        }
    }

    protected void prepareSql() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator(parameters);
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config,
                evaluator);
        SqlFile sqlFile = config.sqlFileRepository().getSqlFile(sqlFilePath,
                config.dialect());
        config.jdbcLogger().logSqlFile(callerClassName, callerMethodName,
                sqlFile);
        SqlNode sqlNode = config.dialect().transformSelectSqlNode(
                sqlFile.getSqlNode(), options);
        sql = sqlBuilder.build(sqlNode);
    }

    @Override
    public void complete() {
    }

    @Override
    public SelectOptions getOptions() {
        return options;
    }

    public void setOptions(SelectOptions options) {
        this.options = options;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public void addParameter(String name, Object value) {
        assertNotNull(name, value);
        parameters.put(name, value);
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
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
    public String getMethodName() {
        return callerMethodName;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    @Override
    public String toString() {
        return sql != null ? sql.toString() : null;
    }

}
