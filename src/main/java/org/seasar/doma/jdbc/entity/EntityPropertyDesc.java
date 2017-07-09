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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * エンティティのプロパティ型を表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <ENTITY>
 *            エンティティの型
 * @param <BASIC>
 *            基本型
 */
public interface EntityPropertyDesc<ENTITY, BASIC> {

    /**
     * プロパティの名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * カラム名を返します。
     * <p>
     * ネーミング規約が適用されます。
     * 
     * @param namingFunction
     *            ネーミング規約を適用する関数
     * @return カラム名
     * @since 2.2.0
     */
    String getColumnName(BiFunction<NamingType, String, String> namingFunction);

    /**
     * カラム名を返します。
     * <p>
     * ネーミング規約が適用されます。
     * <p>
     * 引用符が適用されます。
     * 
     * @param namingFunction
     *            ネーミング規約を適用する関数
     * @param quoteFunction
     *            引用符を適用する関数
     * @return カラム名
     * @since 2.2.0
     */
    String getColumnName(BiFunction<NamingType, String, String> namingFunction,
            Function<String, String> quoteFunction);

    /**
     * カラム名において引用符が必要とされるかどうかを返します。
     * 
     * @return 引用符が必要とされる場合 {@code true}
     */
    boolean isQuoteRequired();

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
     * プロパティを作ります。
     * 
     * @return プロパティ
     */
    Property<ENTITY, BASIC> createProperty();

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
    void copy(ENTITY dest, ENTITY src);

}
