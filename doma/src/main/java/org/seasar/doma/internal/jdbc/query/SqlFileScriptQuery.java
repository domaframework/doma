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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.net.URL;

import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlFileNotFoundException;

/**
 * @author taedium
 * 
 */
public class SqlFileScriptQuery implements ScriptQuery {

    protected Config config;

    protected String sqlFilePath;

    protected String callerClassName;

    protected String callerMethodName;

    protected String blockDelimiter;

    protected boolean haltOnError;

    protected URL sqlFileUrl;

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
    }

    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    @Override
    public void prepare() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName,
                blockDelimiter);
        sqlFileUrl = ResourceUtil.getResource(sqlFilePath);
        if (sqlFileUrl == null) {
            throw new SqlFileNotFoundException(sqlFilePath);
        }
        if (blockDelimiter.isEmpty()) {
            blockDelimiter = config.getDialect().getScriptBlockDelimiter();
        }
    }

    @Override
    public void complete() {
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
        assertUnreachable();
        return null;
    }

    @Override
    public String getSqlFilePath() {
        return sqlFilePath;
    }

    @Override
    public URL getSqlFileUrl() {
        return sqlFileUrl;
    }

    @Override
    public String getBlockDelimiter() {
        return blockDelimiter;
    }

    @Override
    public boolean getHaltOnError() {
        return haltOnError;
    }

}
