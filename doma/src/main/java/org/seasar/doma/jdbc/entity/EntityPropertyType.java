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

/**
 * エンティティのプロパティ型を表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <E>
 *            エンティティの型
 * @param <P>
 *            プロパティの型
 * @param <V>
 *            値の型
 */
public interface EntityPropertyType<E, P, V> {

    /**
     * プロパティの名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    String getColumnName();

    /**
     * 識別子かどうかを返します。
     * 
     * @return 識別子の場合 {@code true}
     */
    boolean isId();

    /**
     * バージョンかどうかを返します。
     * 
     * @return バージョンの場合 {@code true}
     */
    boolean isVersion();

    /**
     * 挿入可能かどうかを返します。
     * 
     * @return 挿入可能の場合 {@code true}
     */
    boolean isInsertable();

    /**
     * 更新可能かどうかを返します。
     * 
     * @return 更新可能の場合 {@code true}
     */
    boolean isUpdatable();

    /**
     * プロパティのアクセサです。
     * 
     * @return プロパティのアクセサ
     */
    Accessor<E, P, V> getAccessor();

    /**
     * プロパティの値をコピーします。
     * <p>
     * deep copy を行います。 ただし、{@code Blob} など JDBC に依存する値はコピーしません。
     * 
     * @param dest
     *            コピー先のエンティティ
     * @param src
     *            コピー元のエンティティ
     */
    void copy(E dest, E src);

}
