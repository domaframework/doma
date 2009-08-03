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
package org.seasar.doma.jdbc.entity;

import java.util.List;


/**
 * エンティティを表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <I>
 *            このインタフェースの実装がもつ外部インタフェースの型
 */
public interface Entity<I> {

    /**
     * エンティティの名前を返します。
     * 
     * @return エンティティの名前
     */
    String __getName();

    /**
     * カタログ名を返します。
     * 
     * @return カタログ名、存在しない場合 {@code null}
     */
    String __getCatalogName();

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名、存在しない場合 {@code null}
     */
    String __getSchemaName();

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名、存在しない場合 {@code null}
     */
    String __getTableName();

    /**
     * 生成される識別子プロパティを返します。
     * 
     * @return 生成される識別子プロパティ、存在しない場合は {@code null}
     */
    GeneratedIdProperty<?> __getGeneratedIdProperty();

    /**
     * バージョンプロパティを返します。
     * 
     * @return バージョンプロパティ、存在しない場合は {@code null}
     */
    VersionProperty<?> __getVersionProperty();

    /**
     * プロパティ名に対応するエンティティプロパティを返します。
     * 
     * @param __name
     *            プロパティ名
     * @return エンティティプロパティ
     */
    EntityProperty<?> __getEntityProperty(String __name);

    /**
     * エンティティプロパティのリストを返します。
     * 
     * @return エンティティプロパティのリスト
     */
    List<EntityProperty<?>> __getEntityProperties();

    /**
     * このインスタンスを外部インタフェースとして返します。
     * 
     * @return このインスタンス
     */
    I __asInterface();

    /**
     * 挿入処理の前処理を行います。
     */
    void __preInsert();

    /**
     * 更新処理の前処理を行います。
     */
    void __preUpdate();

    /**
     * 削除処理の前処理を行います。
     */
    void __preDelete();
}
