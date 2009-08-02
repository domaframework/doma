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
package org.seasar.doma.domain;

import org.seasar.doma.DomaNullPointerException;

/**
 * ドメイン（値の定義域）を表現します。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * 
 * @param <V>
 *            値の型
 * @param <D>
 *            ドメインの型
 */
public interface Domain<V, D extends Domain<V, D>> {

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
     * ドメインを設定します。
     * 
     * @param other
     *            ドメイン
     * @throws DomaNullPointerException
     *             ドメイン が {@code null} の場合
     */
    void setDomain(D other) throws DomaNullPointerException;

    /**
     * 値が {@code null} かどうかを返します。
     * 
     * @return {@code null} の場合 {@code true}
     */
    boolean isNull();

    /**
     * 値が {@code null} でないかどうかを返します。
     * 
     * @return {@code null} でない場合 {@code true}
     */
    boolean isNotNull();

    /**
     * 値が変更されているかどうかを返します。
     * 
     * @return 変更されている場合 {@code true}
     */
    boolean isChanged();

    /**
     * 値が変更されているかどうかを設定します。
     * 
     * @param changed
     *            変更されているマークしたい場合 {@code true}
     */
    void setChanged(boolean changed);

    /**
     * 値のクラスを返します。
     * 
     * @return 値のクラス
     */
    Class<V> getValueClass();

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
    <R, P, TH extends Throwable> R accept(DomainVisitor<R, P, TH> visitor, P p)
            throws TH, DomaNullPointerException;

}
