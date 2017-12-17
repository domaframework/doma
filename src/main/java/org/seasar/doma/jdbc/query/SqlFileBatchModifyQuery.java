package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityDesc;

public abstract class SqlFileBatchModifyQuery<ELEMENT> extends AbstractQuery
        implements BatchModifyQuery {

    protected static final String[] EMPTY_STRINGS = new String[] {};

    protected final Class<ELEMENT> elementClass;

    protected final SqlKind kind;

    protected String sqlFilePath;

    protected String parameterName;

    protected SqlFile sqlFile;

    protected boolean optimisticLockCheckRequired;

    protected boolean executable;

    protected SqlExecutionSkipCause sqlExecutionSkipCause = SqlExecutionSkipCause.BATCH_TARGET_NONEXISTENT;

    protected int batchSize;

    protected SqlLogType sqlLogType;

    protected String[] includedPropertyNames = EMPTY_STRINGS;

    protected String[] excludedPropertyNames = EMPTY_STRINGS;

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
        super.prepare();
        assertNotNull(method, sqlFilePath, parameterName, elements, sqls);
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
                Collections.singletonMap(parameterName, value),
                config.getDialect().getExpressionFunctions(), config.getClassHelper());
        NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config, kind,
                sqlFile.getPath(), evaluator, sqlLogType, this::expandColumns,
                this::populateValues);
        PreparedSql sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
        sqls.add(sql);
    }

    protected List<String> expandColumns(ExpandNode node) {
        throw new UnsupportedOperationException();
    }

    protected void populateValues(PopulateNode node, SqlContext context) {
        throw new UnsupportedOperationException();
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
            this.elements = new ArrayList<ELEMENT>((Collection<ELEMENT>) elements);
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

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    public void setIncludedPropertyNames(String... includedPropertyNames) {
        this.includedPropertyNames = includedPropertyNames;
    }

    public void setExcludedPropertyNames(String... excludedPropertyNames) {
        this.excludedPropertyNames = excludedPropertyNames;
    }

    public abstract void setEntityDesc(EntityDesc<ELEMENT> entityDesc);

    @Override
    public PreparedSql getSql() {
        return sqls.get(0);
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
