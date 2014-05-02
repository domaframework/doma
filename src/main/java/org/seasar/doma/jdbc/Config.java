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

import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.message.Message;

/**
 * JDBCに関する設定です。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 */
public interface Config {

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    DataSource getDataSource();

    /**
     * RDBMSの方言を返します。
     * 
     * @return RDBMSの方言
     */
    Dialect getDialect();

    /**
     * データソース名を返します。
     * <p>
     * データソースを複数扱う場合、データソースごとに異なる名前を返さなければいけません。この値は、シーケンスやテーブルを使用した識別子の自動生成機能で、
     * 生成した識別子をデータソースごとに管理するために使用されます。
     * 
     * @return データソース名
     */
    default String getDataSourceName() {
        return getClass().getName();
    }

    /**
     * SQLファイルのリポジトリを返します。
     * 
     * @return SQLファイルのリポジトリ
     */
    default SqlFileRepository getSqlFileRepository() {
        return ConfigSupport.defaultSqlFileRepository;
    }

    /**
     * JDBCロガーを返します。
     * 
     * @return JDBCロガー
     */
    default JdbcLogger getJdbcLogger() {
        return ConfigSupport.defaultJdbcLogger;
    }

    /**
     * {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラーを返します。
     * <p>
     * {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラーは、テーブルを使用した識別子の自動生成機能において、
     * テーブルの更新処理を新しいトランザクション内で実行するために使われます。
     * 
     * @return {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラー
     */
    default RequiresNewController getRequiresNewController() {
        return ConfigSupport.defaultRequiresNewController;
    }

    /**
     * クラスのヘルパーを返します。
     * 
     * @return クラスのヘルパー
     * @since 1.27.0
     */
    default ClassHelper getClassHelper() {
        return ConfigSupport.defaultClassHelper;
    }

    /**
     * {@link Command} の実装クラスのファクトリを返します。
     * 
     * @return {@link Command} の実装クラスのファクトリ
     * @since 2.0.0
     */
    default CommandImplementors getCommandImplementors() {
        return ConfigSupport.defaultCommandImplementors;
    }

    /**
     * {@link Query} の実装クラスのファクトリを返します。
     * 
     * @return {@link Query} の実装クラスのファクトリ
     * @since 2.0.0
     */
    default QueryImplementors getQueryImplementors() {
        return ConfigSupport.defaultQueryImplementors;
    }

    /**
     * 例外に含めるSQLログのタイプを返します。
     * 
     * @return SQLログのタイプ
     * @since 1.22.0
     */
    default ExceptionSqlLogType getExceptionSqlLogType() {
        return ExceptionSqlLogType.FORMATTED_SQL;
    }

    /**
     * 未知のカラムのハンドラを返します。
     * 
     * @return 未知のカラムのハンドラ
     * @since 2.0.0
     */
    default UnknownColumnHandler getUnknownColumnHandler() {
        return ConfigSupport.defaultUnknownColumnHandler;
    }

    /**
     * マップのキーのネーミング規約のコントローラを返します。
     * 
     * @return マップのキーのネーミング規約のコントローラ
     */
    default MapKeyNaming getMapKeyNaming() {
        return ConfigSupport.defaultMapKeyNaming;
    }

    /**
     * トランザクションマネジャーを返します。
     * <p>
     * デフォルトの実装では {@link UnsupportedOperationException} をスローします。
     * 
     * @return トランザクションマネジャー
     * @throws UnsupportedOperationException
     *             {@link TransactionManager} のインタフェースを使ったトランザクションをサポートしない場合
     * @since 2.0.0
     */
    default TransactionManager getTransactionManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * 最大行数の制限値を返します。
     * <p>
     * 0以下の値は、 {@link Statement#setMaxRows(int)}へは渡されません。
     * 
     * @return 最大行数の制限値
     * @see Statement#setMaxRows(int)
     */
    default int getMaxRows() {
        return 0;
    }

    /**
     * フェッチサイズを返します。
     * <p>
     * 0以下の値は、 {@link Statement#setFetchSize(int)}へは渡されません。
     * 
     * @return フェッチサイズ
     * @see Statement#setFetchSize(int)
     */
    default int getFetchSize() {
        return 0;
    }

    /**
     * クエリタイムアウト（秒）を返します。
     * <p>
     * 0以下の値は、 {@link Statement#setQueryTimeout(int)}へは渡されません。
     * 
     * @return クエリタイムアウト（秒）
     * @see Statement#setQueryTimeout(int)
     */
    default int getQueryTimeout() {
        return 0;
    }

    /**
     * バッチサイズを返します。
     * <p>
     * {@literal 1} 以下の値は、 {@literal 1} とみなされます。
     * 
     * {@link PreparedStatement#executeBatch()} を実行する際のバッチサイズです。
     * バッチ対象の数がバッチサイズを上回る場合、バッチサイズの数だけ {@link PreparedStatement#addBatch()}
     * を呼び出し、 {@link PreparedStatement#executeBatch()} を実行するということを繰り返します。
     * 
     * @return バッチサイズを返します。
     * @see PreparedStatement#addBatch()
     */
    default int getBatchSize() {
        return 0;
    }

    /**
     * {@link ConfigProvider} から {@link Config} を取得します。
     * 
     * @param provider
     *            {@link ConfigProvider} を実装していることを期待されるオブジェクト
     * @return {@link ConfigProvider} が返した {@link Config}
     * @throws DomaIllegalArgumentException
     *             {@code provider} が {@link ConfigProvider} でない場合
     * @since 2.0.0
     */
    static Config get(Object provider) {
        if (provider instanceof ConfigProvider) {
            ConfigProvider p = (ConfigProvider) provider;
            return p.getConfig();
        }
        throw new DomaIllegalArgumentException("provider",
                Message.DOMA2218.getMessage("provider",
                        ConfigProvider.class.getName()));
    }
}
