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
import java.util.Set;

/**
 * エンティティのメタデータを表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <E>
 *            エンティティの型
 */
public interface EntityMeta<E> {

    /**
     * エンティティの名前を返します。
     * 
     * @return エンティティの名前
     */
    String getName();

    /**
     * カタログ名を返します。
     * 
     * @return カタログ名、存在しない場合 {@code null}
     */
    String getCatalogName();

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名、存在しない場合 {@code null}
     */
    String getSchemaName();

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名、存在しない場合 {@code null}
     */
    String getTableName();

    /**
     * 生成される識別子プロパティを返します。
     * 
     * @return 生成される識別子プロパティ、存在しない場合は {@code null}
     */
    GeneratedIdPropertyMeta<?> getGeneratedIdProperty();

    /**
     * バージョンプロパティを返します。
     * 
     * @return バージョンプロパティ、存在しない場合は {@code null}
     */
    VersionPropertyMeta<?> getVersionProperty();

    /**
     * プロパティ名に対応するプロパティメタデータを返します。
     * 
     * @param __name
     *            プロパティ名
     * @return エンティティプロパティ
     */
    EntityPropertyMeta<?> getPropertyMeta(String __name);

    /**
     * エンティティプロパティのリストを返します。
     * 
     * @return エンティティプロパティのリスト
     */
    List<EntityPropertyMeta<?>> getPropertyMetas();

    /**
     * エンティティを返します。
     * 
     * @return エンティティ
     */
    E getEntity();

    Class<E> getEntityClass();

    Set<String> getModifiedProperties();

    void refreshEntity();

    /**
     * 挿入処理の前処理を行います。
     */
    void preInsert();

    /**
     * 更新処理の前処理を行います。
     */
    void preUpdate();

    /**
     * 削除処理の前処理を行います。
     */
    void preDelete();
}
