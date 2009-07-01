package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQuery<T, R extends Domain<T, ?>> implements
        CreateQuery<R> {

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    protected R result;

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
    }

    public void setResult(R result) {
        this.result = result;
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
        return -1;
    }

    @Override
    public Sql<?> getSql() {
        return null;
    }

}
