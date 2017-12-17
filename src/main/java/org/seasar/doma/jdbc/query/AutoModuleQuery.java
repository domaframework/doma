package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;
import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;

public abstract class AutoModuleQuery extends AbstractQuery implements ModuleQuery {

    protected CallableSql sql;

    protected String catalogName;

    protected String schemaName;

    protected String moduleName;

    protected String qualifiedName;

    protected boolean isQuoteRequired;

    protected final List<SqlParameter> parameters = new ArrayList<>();

    protected SqlLogType sqlLogType;

    protected void prepareQualifiedName() {
        Function<String, String> mapper = isQuoteRequired ? config.getDialect()::applyQuote
                : Function.identity();
        qualifiedName = DatabaseObjectUtil.getQualifiedName(mapper, catalogName, schemaName,
                moduleName);
    }

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.getQueryTimeout();
        }
    }

    @Override
    public void complete() {
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    protected void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setQuoteRequired(boolean isQuoteRequired) {
        this.isQuoteRequired = isQuoteRequired;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    public void addParameter(SqlParameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public CallableSql getSql() {
        return sql;
    }

    @Override
    public SqlLogType getSqlLogType() {
        return sqlLogType;
    }

}
