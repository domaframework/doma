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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * @param <ELEMENT>
 *            リストの要素
 */
public abstract class SqlFileBatchModifyQuery<ELEMENT> implements
        BatchModifyQuery {

    protected final Class<ELEMENT> elementClass;

    protected final SqlKind kind;

    protected Method method;

    protected Config config;

    protected String sqlFilePath;

    protected String parameterName;

    protected String callerClassName;

    protected String callerMethodName;

    protected SqlFile sqlFile;

    protected boolean optimisticLockCheckRequired;

    protected boolean executable;

    protected SqlExecutionSkipCause sqlExecutionSkipCause = SqlExecutionSkipCause.BATCH_TARGET_NONEXISTENT;

    protected int queryTimeout;

    protected int batchSize;

    protected SqlLogType sqlLogType;

    protected List<ELEMENT> elements;

    protected ELEMENT currentEntity;

    protected List<PreparedSql> sqls;

    protected SqlFileBatchModifyQuery(Class<ELEMENT> elementClass, SqlKind kind) {
        assertNotNull(elementClass, kind);
        this.elementClass = elementClass;
        this.kind = kind;
    }

    @Override
    public void prepare() {
        assertNotNull(method, config, sqlFilePath, parameterName,
                callerClassName, callerMethodName, elements, sqls);
    }

    protected void prepareSqlFile() {
        sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath,
                config.getDialect());
    }

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.getQueryTimeout();
        }
        if (batchSize <= 0) {
            batchSize = config.getBatchSize();
        }
    }

    protected void prepareSql() {
        Value value = new Value(elementClass, currentEntity);
        ExpressionEvaluator evaluator = new ExpressionEvaluator(
                Collections.singletonMap(parameterName, value), config
                        .getDialect().getExpressionFunctions(),
                config.getClassHelper());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config,
                kind, sqlFile.getPath(), evaluator, sqlLogType);
        PreparedSql sql = sqlBuilder.build(sqlFile.getSqlNode());
        sqls.add(sql);
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setElements(Iterable<ELEMENT> elements) {
        assertNotNull(elements);
        if (elements instanceof Collection<?>) {
            this.elements = new ArrayList<ELEMENT>(
                    (Collection<ELEMENT>) elements);
        } else {
            this.elements = new ArrayList<ELEMENT>();
            for (ELEMENT element : elements) {
                this.elements.add(element);
            }
        }
        this.sqls = new ArrayList<PreparedSql>(this.elements.size());
    }

    public List<ELEMENT> getEntities() {
        return elements;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    public abstract void setEntityType(EntityType<ELEMENT> entityType);

    @Override
    public PreparedSql getSql() {
        return sqls.get(0);
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
    public List<PreparedSql> getSqls() {
        return sqls;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public boolean isOptimisticLockCheckRequired() {
        return optimisticLockCheckRequired;
    }

    @Override
    public boolean isAutoGeneratedKeysSupported() {
        return false;
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }

    @Override
    public SqlExecutionSkipCause getSqlExecutionSkipCause() {
        return sqlExecutionSkipCause;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public SqlLogType getSqlLogType() {
        return sqlLogType;
    }

    @Override
    public String toString() {
        return sqls.toString();
    }

}
