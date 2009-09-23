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
 * @param <W>
 *            ドメインの型
 */
public interface Wrapper<V, W extends Wrapper<V, W>> {

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
