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

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * JDBCに関する設定です。
 * <p>
 * このインタフェースの実装は、引数なしの {@code public} なコンストラクタを持たなければいけません。
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
     * データソース名を返します。
     * <p>
     * データソースを複数扱う場合、データソースごとに異なる名前を返さなければいけません。この値は、シーケンスやテーブルを使用した識別子の自動生成機能で、
     * 生成した識別子をデータソースごとに管理するために使用されます。
     * 
     * @return データソース名
     */
    String getDataSourceName();

    /**
     * RDBMSの方言を返します。
     * 
     * @return RDBMSの方言
     */
    Dialect getDialect();

    /**
     * SQLファイルのリポジトリを返します。
     * 
     * @return SQLファイルのリポジトリ
     */
    SqlFileRepository getSqlFileRepository();

    /**
     * JDBCロガーを返します。
     * 
     * @return JDBCロガー
     */
    JdbcLogger getJdbcLogger();

    /**
     * {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラーを返します。
     * <p>
     * {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラーは、テーブルを使用した識別子の自動生成機能において、
     * テーブルの更新処理を新しいトランザクション内で実行するために使われます。
     * 
     * @return {@code REQUIRES_NEW}のトランザクション属性を制御するコントローラー
     */
    RequiresNewController getRequiresNewController();

    /**
     * クラスのヘルパーを返します。
     * 
     * @return クラスのヘルパー
     * @since 1.27.0
     */
    ClassHelper getClassHelper();

    /**
     * 例外に含めるSQLログのタイプを返します。
     * 
     * @return SQLログのタイプ
     * @since 1.22.0
     */
    ExceptionSqlLogType getExceptionSqlLogType();

    /**
     * 最大行数の制限値を返します。
     * <p>
     * 0以下の値は、 {@link Statement#setMaxRows(int)}へは渡されません。
     * 
     * @return 最大行数の制限値
     * @see Statement#setMaxRows(int)
     */
    int getMaxRows();

    /**
     * フェッチサイズを返します。
     * <p>
     * 0以下の値は、 {@link Statement#setFetchSize(int)}へは渡されません。
     * 
     * @return フェッチサイズ
     * @see Statement#setFetchSize(int)
     */
    int getFetchSize();

    /**
     * クエリタイムアウト（秒）を返します。
     * <p>
     * 0以下の値は、 {@link Statement#setQueryTimeout(int)}へは渡されません。
     * 
     * @return クエリタイムアウト（秒）
     * @see Statement#setQueryTimeout(int)
     */
    int getQueryTimeout();

    /**
     * バッチサイズを返します。
     * <p>
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
    int getBatchSize();

    /**
     * Entityに定義が存在しないカラムが結果セットに含まれていた場合に、無視するかどうかを示すフラグです。
     * @return Entityに定義が存在しないカラムを無視するならば true, そうでなければ false.
     */
    boolean ignoreUnknownColumn();

}
