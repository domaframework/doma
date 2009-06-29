package org.seasar.doma.internal.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.sql.CallableSql;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.jdbc.Config;


/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQuery implements ModuleQuery {

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    protected CallableSql sql;

    protected int queryTimeout;

    protected final List<CallableSqlParameter> parameters = new ArrayList<CallableSqlParameter>();

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.queryTimeout();
        }
    }

    public void setConfig(Config config) {
        this.config = config;
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

    public void addParameter(CallableSqlParameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public Config getConfig() {
        return config;
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
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public CallableSql getSql() {
        return sql;
    }

}
