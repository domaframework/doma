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

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 */
public class SqlFileScriptQuery implements ScriptQuery {

    protected Config config;

    protected String scriptFilePath;

    protected String callerClassName;

    protected String callerMethodName;

    protected String blockDelimiter;

    protected boolean haltOnError;

    protected URL scriptFileUrl;

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
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
        assertNotNull(config, scriptFilePath, callerClassName,
                callerMethodName, blockDelimiter);
        assertTrue(scriptFilePath.startsWith(Constants.SCRIPT_PATH_PREFIX));
        assertTrue(scriptFilePath.endsWith(Constants.SCRIPT_PATH_SUFFIX));

        String dbmsSpecificPath = ScriptFileUtil.convertToDbmsSpecificPath(
                scriptFilePath, config.getDialect());
        scriptFileUrl = ResourceUtil.getResource(dbmsSpecificPath);
        if (scriptFileUrl != null) {
            scriptFilePath = dbmsSpecificPath;
        } else {
            scriptFileUrl = ResourceUtil.getResource(scriptFilePath);
            if (scriptFileUrl == null) {
                throw new ScriptFileNotFoundException(scriptFilePath);
            }
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
    public String getScriptFilePath() {
        return scriptFilePath;
    }

    @Override
    public URL getScriptFileUrl() {
        return scriptFileUrl;
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
