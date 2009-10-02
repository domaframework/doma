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
package org.seasar.doma.jdbc;

import org.seasar.doma.Delegate;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * ネーミング規約です。
 * <p>
 * <ul>
 * <li>エンティティ名とは {@link Entity} が注釈されたインタフェースの単純名です。
 * <li>プロパティ名とは {@code Entity} が注釈されたインタフェースのメンバメソッド（ただし、 {@link Delegate}
 * が注釈されたものは除く ）の名前です。
 * <li>テーブル名とは、データベースのテーブルの名前です。カタログ名やスキーマ名は含みません。
 * <li>カラム名とは、データベースのテーブルのカラムの名前です。
 * </ul>
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 */
public interface NamingConvention {

    /**
     * エンティティ名からテーブル名へ変換します。
     * <p>
     * 更新系SQLの自動生成時、テーブル名が明示されていない場合に呼び出されます。
     * 
     * @param entityName
     *            エンティティ名
     * @param dialect
     *            方言
     * @return テーブル名
     * @throws DomaNullPointerException
     *             いずれかの引数が {@code null} の場合
     */
    String fromEntityToTable(String entityName, Dialect dialect);

    /**
     * プロパティ名からカラム名へ変換します。
     * <p>
     * 更新系SQLの自動生成時、カラム名が明示されていない場合に呼び出されます。また、
     * 検索系SQLの結果セットをプロパティにマッピングする際に呼び出されます。
     * 
     * @param propertyName
     *            プロパティ名
     * @param dialect
     *            方言
     * @return カラム名
     * @throws DomaNullPointerException
     *             いずれかの引数が {@code null} の場合
     */
    String fromPropertyToColumn(String propertyName, Dialect dialect);

}
