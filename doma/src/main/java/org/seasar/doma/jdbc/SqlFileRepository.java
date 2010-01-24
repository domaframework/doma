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

import org.seasar.doma.Dao;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * {@link SqlFile} のリポジトリです。
 * <p>
 * SQLファイルのパスは次の制約を満たさねばなりません。
 * <ul>
 * <li>'META-INF/'で始まる。
 * <li>その後ろに、 {@link Dao} が注釈されたインタフェースの完全修飾名の {@code .} を {@code /} に置換したものが続く。
 * <li>その後ろに、{@code /} が続く。
 * <li>その後ろに、{@link Dao} が注釈されたインタフェースのメンバメソッド名が続く。
 * <li>'.sql'で終わる。ただし、'.sql'の前にハイフン区切りでRDBMSの名前を指定可能。
 * </ul>
 * 
 * <h5>SQLファイルのパスの例</h5>
 * <p>
 * {@code META-INF/org/example/ExampleDao/selectAll.sql}
 * <p>
 * このインタフェースの実装クラスは、まず、RDBMS固有のSQLファイルがあるかどうか調べ、あればそちらを使用しなければいけません。
 * RDBMS固有のSQLファイルのパスは、 '.sql'の直前に次の2種類の文字列を挿入することで求められます。
 * <p>
 * <ul>
 * <li>{@code -}（ハイフン）
 * <li>{@link Dialect#getName()} で返される値。
 * </ul>
 * <h5>RDBMS固有のSQLファイルのパスの例</h5>
 * <p>
 * {@code META-INF/org/example/ExampleDao/selectAll-oracle.sql}
 * <p>
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 */
public interface SqlFileRepository {

    /**
     * SQLファイルを返します。
     * 
     * @param path
     *            SQLファイルのパス
     * @param dialect
     *            方言
     * @return SQLファイル
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code path} が'META-INF/'で始まらない場合、 もしくは、{@code path} 
     *             が'.sql'で終わらない場合
     * @throws SqlFileNotFoundException
     *             SQLファイルが見つからない場合
     * @throws JdbcException
     *             上記以外で例外が発生した場合
     */
    SqlFile getSqlFile(String path, Dialect dialect);

}
