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
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.net.URL;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 */
public class SqlFileScriptQuery extends AbstractQuery implements ScriptQuery {

    protected String scriptFilePath;

    protected String blockDelimiter;

    protected boolean haltOnError;

    protected URL scriptFileUrl;

    protected SqlLogType sqlLogType;

    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }

    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(scriptFilePath, blockDelimiter);
        assertTrue(scriptFilePath.startsWith(Constants.SCRIPT_PATH_PREFIX));
        assertTrue(scriptFilePath.endsWith(Constants.SCRIPT_PATH_SUFFIX));

        String dbmsSpecificPath = ScriptFileUtil.convertToDbmsSpecificPath(scriptFilePath,
                config.getDialect());
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

    public SqlLogType getSqlLogType() {
        return sqlLogType;
    }

}
