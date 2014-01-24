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

    /**
     * {@link SqlFile} のリポジトリ
     * 
     * @since 1.1.0
     */
    protected static SqlFileRepository defaultSqlFileRepository = new GreedyCacheSqlFileRepository();

    /**
     * DBCに関する処理を記録するロガー
     * 
     * @since 1.1.0
     */
    protected static JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

    /**
     * {@literal REQUIRES_NEW} の属性をもつトランザクションを制御するコントローラ
     * 
     * @since 1.1.0
     */
    protected static RequiresNewController defaultRequiresNewController = new NullRequiresNewController();

    /**
     * クラスのヘルパー
     * 
     * @since 1.27.0
     */
    protected static ClassHelper defaultClassHelper = new DefaultClassHelper();

    /**
     * インスタンスを構築します。
     */
    protected DomaAbstractConfig() {
    }

    @Override
    public String getDataSourceName() {
        return getClass().getName();
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return defaultSqlFileRepository;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return defaultJdbcLogger;
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return defaultRequiresNewController;
    }

    @Override
    public ClassHelper getClassHelper() {
        return defaultClassHelper;
    }

    @Override
    public ExceptionSqlLogType getExceptionSqlLogType() {
        return ExceptionSqlLogType.FORMATTED_SQL;
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

    @Override
    public int getBatchSize() {
        return 10;
    }

    @Override
    public boolean ignoreUnknownColumn() {
        return false;
    }
}
