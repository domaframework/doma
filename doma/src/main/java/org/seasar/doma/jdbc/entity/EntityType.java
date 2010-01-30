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
package org.seasar.doma.jdbc.entity;

import java.util.List;

import org.seasar.doma.jdbc.criteria.TableCriterion;

/**
 * エンティティ型を表します。
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
public interface EntityType<E> extends TableCriterion<E> {

    /**
     * エンティティの名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * カタログ名を返します。
     * 
     * @return カタログ名
     */
    String getCatalogName();

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    String getSchemaName();

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    String getTableName();

    /**
     * 完全修飾されたテーブル名を返します。
     * 
     * @return 完全修飾されたテーブル名
     */
    String getQualifiedTableName();

    /**
     * ネーミング規約を返します。
     * 
     * @return ネーミング規約
     */
    NamingType getNamingType();

    /**
     * 自動生成される識別子のプロパティ型を返します。
     * 
     * @return 自動生成される識別子のプロパティ型
     */
    GeneratedIdPropertyType<E, ?> getGeneratedIdPropertyType();

    /**
     * バージョンのプロパティ型を返します。
     * 
     * @return バージョンのプロパティ型
     */
    VersionPropertyType<E, ?> getVersionPropertyType();

    /**
     * 識別子のプロパティ型のリストを返します。
     * 
     * @return 識別子のプロパティ型のリスト
     */
    List<EntityPropertyType<E, ?>> getIdPropertyTypes();

    /**
     * 名前を指定してプロパティ型を返します。
     * 
     * @param __name
     *            プロパティ名
     * @return プロパティ名、存在しない場合 {@code null}
     */
    EntityPropertyType<E, ?> getEntityPropertyType(String __name);

    /**
     * プロパティ型のリストを返します。
     * 
     * @return プロパティ型のリスト
     */
    List<EntityPropertyType<E, ?>> getEntityPropertyTypes();

    /**
     * エンティティをインスタンス化して返します。
     * 
     * @return エンティティ
     */
    E newEntity();

    /**
     * エンティティのクラスを返します。
     * 
     * @return エンティティのクラス
     */
    Class<E> getEntityClass();

    /**
     * 現在の状態を保存します。
     * 
     * @param entity
     *            現在の状態
     */
    void saveCurrentStates(E entity);

    /**
     * 元の状態を返します。
     * 
     * @param entity
     *            元の状態
     * @return 元の状態、存在しない場合 {@code null}
     */
    E getOriginalStates(E entity);

    /**
     * 挿入処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preInsert(E entity);

    /**
     * 更新処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preUpdate(E entity);

    /**
     * 削除処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preDelete(E entity);
}
