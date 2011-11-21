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
package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * 値のラッパーです。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * 
 * @param <V>
 *            値の型
 */
public interface Wrapper<V> {

    /**
     * 値を返します。
     * 
     * @return 値、{@code null} でありうる
     */
    V get();

    /**
     * 値を設定します。
     * 
     * @param value
     *            値
     */
    void set(V value);

    /**
     * 値のコピーを返します。
     * 
     * @return 値のコピー
     */
    V getCopy();

    /**
     * 値の型がプリミティブ型のボックス型であればプリミティブ型のデフォルト値をボックスした値を返します。
     * 
     * @return 
     *         値の型がプリミティブ型のボックス型のであればプリミティブ型のデフォルト値をボックスした値、値の型がプリミティブ型のボックス型でない場合
     *         {@code null}
     * @since 1.5.0
     */
    V getDefault();

    /**
     * 等しい値を持っている場合 {@code true} を返します。
     * 
     * @param other
     *            値
     * @return 等しい値を持っている場合 {@code true}
     */
    boolean hasEqualValue(Object other);

    void setAccessor(Accessor<V> accessor);

    /**
     * ビジターを受け入れます。
     * 
     * @param <R>
     *            戻り値の型
     * @param <P>
     *            パラメータの型
     * @param <TH>
     *            例外の型
     * @param visitor
     *            ビジター
     * @param p
     *            パラメータ
     * @return 戻り値
     * @throws TH
     *             例外
     * @throws DomaNullPointerException
     *             ビジターが {@code null} の場合
     */
    <R, P, TH extends Throwable> R accept(WrapperVisitor<R, P, TH> visitor, P p)
            throws TH;

}
