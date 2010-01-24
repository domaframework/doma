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
package org.seasar.doma.jdbc;

/**
 * {@link Config} の骨格実装です。
 * <p>
 * 多くの場合、アプリケーションの要件に応じた実装を作成し使用することを検討してください。
 * 
 * @author taedium
 * 
 */
public abstract class DomaAbstractConfig implements Config {

    private static SqlFileRepository sqlFileRepository = new GreedyCacheSqlFileRepository();

    private static JdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();

    private static RequiresNewController requiresNewController = new NullRequiresNewController();

    @Override
    public String getDataSourceName() {
        return getClass().getName();
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return requiresNewController;
    }

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public int getQueryTimeout() {
        return 0;
    }

    public int getBatchSize() {
        return 10;
    }
}
