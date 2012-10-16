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
package org.seasar.doma.jdbc.domain;

/**
 * ドメインのメタタイプです。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * @since 1.8.0
 * @param <V>
 *            ドメインが扱う値の型
 * @param <D>
 *            ドメインの型
 */
public interface DomainType<V, D> {

    /**
     * ドメインを生成します。
     * 
     * @param value
     *            値
     * @return ドメイン
     */
    D newDomain(V value);

    /**
     * 値のクラスを返します。
     * 
     * @return 値のクラス
     * @since 1.25.0
     */
    Class<V> getValueClass();

    /**
     * ドメインクラスを返します。
     * 
     * @return ドメインクラス
     */
    Class<D> getDomainClass();

    /**
     * ドメインのラッパーを返します。
     * 
     * @param domain
     *            ドメイン
     * @return ドメインのラッパー
     */
    DomainWrapper<V, D> getWrapper(D domain);
}
