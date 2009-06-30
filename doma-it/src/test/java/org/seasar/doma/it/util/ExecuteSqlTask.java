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
package org.seasar.doma.it.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.seasar.doma.it.util.dialect.MysqlDialect;
import org.seasar.doma.it.util.dialect.OracleDialect;
import org.seasar.doma.it.util.dialect.PostgreDialect;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.env.Env;

/**
 * SQLを実行するタスクです。
 * 
 * @author taedium
 */
public class ExecuteSqlTask extends Task {

    /** 環境名をキー、方言を値とするマップ */
    protected static Map<String, Dialect> dialectMap = new HashMap<String, Dialect>();
    static {
        dialectMap.put("postgres", new PostgreDialect());
        dialectMap.put("mysql", new MysqlDialect());
        dialectMap.put("oracle", new OracleDialect());
    }

    /** エラー時に即座に中止する場合{@code true}、継続する場合{@code false} */
    protected boolean haltOnError = true;

    /** SQLファイル */
    protected File sqlFile = null;

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
        if (sqlFile == null) {
            throw new BuildException("sqlFile not specified.");
        }

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    getClass().getClassLoader());
            Env.setFilePath("env_ut.txt");
            SingletonS2ContainerFactory.setConfigPath("jdbc.dicon");
            SingletonS2ContainerFactory.init();
            try {
                executeSql();
            } finally {
                SingletonS2ContainerFactory.destroy();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * SQLを実行します。
     */
    protected void executeSql() {
        String env = Env.getValue();
        Dialect dialect = dialectMap.get(env);
        if (dialect == null) {
            throw new BuildException("Dialect not found for env value(" + env
                    + ").");
        }

        SqlFileExecutor executor = new SqlFileExecutor(dialect, "UTF-8", ';',
                null);

        S2Container container = SingletonS2ContainerFactory.getContainer();
        DataSource dataSource = (DataSource) container
                .getComponent(DataSource.class);

        SqlExecutionContext context = new SqlExecutionContext(dataSource,
                haltOnError);
        try {
            executor.execute(context, sqlFile);
        } finally {
            context.destroy();
        }
    }
}