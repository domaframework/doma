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
package org.seasar.doma.it.helper.task;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.sql.DataSource;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.seasar.doma.it.helper.dialect.H2ToolDialect;
import org.seasar.doma.it.helper.dialect.HsqldbToolDialect;
import org.seasar.doma.it.helper.dialect.MySqlToolDialect;
import org.seasar.doma.it.helper.dialect.OracleToolDialect;
import org.seasar.doma.it.helper.dialect.PostgresToolDialect;
import org.seasar.doma.it.helper.dialect.StandardToolDialect;
import org.seasar.doma.it.helper.dialect.ToolDialect;
import org.seasar.doma.it.helper.logging.Logger;
import org.seasar.doma.it.helper.sqlfile.SqlExecutionContext;
import org.seasar.doma.it.helper.sqlfile.SqlFileExecutor;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.env.Env;

/**
 * SQLを実行するタスクです。
 * 
 * @author taedium
 */
public class ExecSqlFileTask extends Task {

    /** エラー時に即座に中止する場合{@code true}、継続する場合{@code false} */
    protected boolean haltOnError = true;

    /** SQLファイル */
    protected File sqlFile = null;

    /** データソース */
    protected DataSource dataSource = null;

    /** 方言 */
    protected ToolDialect toolDialect = null;

    /**
     * エラー時に即座に中止する場合{@code true}、継続する場合{@code false}を設定します。
     * 
     * @param haltOnError
     *            エラー時に即座に中止する場合{@code true}、継続する場合{@code false}
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * SQLファイルを設定します。
     * 
     * @param sqlFile
     *            SQLファイル
     */
    public void setSqlFile(File sqlFile) {
        this.sqlFile = sqlFile;
    }

    @Override
    public void execute() throws BuildException {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    getClass().getClassLoader());
            Logger.setDelegate(new TaskLoggerDelegate(this));
            validate();
            prepare();
            run();
        } catch (Throwable throwable) {
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer));
            throw new BuildException(writer.toString());
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    protected void validate() {
        if (sqlFile == null) {
            throw new RuntimeException("sqlFile must not be null.");
        }
        if (!sqlFile.exists()) {
            throw new RuntimeException(sqlFile.getAbsolutePath()
                    + " is not found.");
        }
    }

    protected void prepare() {
        Env.setFilePath("env_ut.txt");
        SingletonS2ContainerFactory.setConfigPath("jdbc.dicon");
        SingletonS2ContainerFactory.init();
        dataSource = SingletonS2Container.getComponent(DataSource.class);
        toolDialect = getToolDialect();
    }

    protected ToolDialect getToolDialect() {
        String dialectName = SingletonS2Container.getComponent(Dialect.class)
                .getName();
        if ("hsqldb".equals(dialectName)) {
            return new HsqldbToolDialect();
        }
        if ("h2".equals(dialectName)) {
            return new H2ToolDialect();
        }
        if ("mysql".equals(dialectName)) {
            return new MySqlToolDialect();
        }
        if ("postgres".equals(dialectName)) {
            return new PostgresToolDialect();
        }
        if ("oracle".equals(dialectName)) {
            return new OracleToolDialect();
        }
        return new StandardToolDialect();
    }

    protected void run() {
        SqlFileExecutor executor = new SqlFileExecutor(toolDialect, "UTF-8",
                ';', null);
        SqlExecutionContext context = new SqlExecutionContext(dataSource,
                haltOnError);
        try {
            executor.execute(context, sqlFile);
        } finally {
            context.destroy();
        }
    }
}