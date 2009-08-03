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
 * 比較可能な {@link Domain} です。
 * 
 * @author taedium
 * 
 * @param <V>
 *            値の型
 * @param <D>
 *            ドメインの型
 */
public interface ComparableDomain<V extends Comparable<? super V>, D extends Domain<V, D> & Comparable<? super D>>
        extends Domain<V, D>, Comparable<D> {

    int compareTo(D other);

    /**
     * このインスタンスの値が比較対象の値と等しい場合 {@code true} を返します。
     * <p>
     * 値が {@code null} 同士である場合、等しいとみなされます。
     * 
     * @param other
     *            比較対象の値
     * @return 等しい場合
     */
    boolean eq(V other);

    /**
     * このインスタンスの値が比較対象のドメインの値と等しい場合 {@code true} を返します。
     * <p>
     * 値が {@code null} 同士である場合、等しいとみなされます。
     * 
     * @param other
     *            比較対象のドメイン
     * @return 等しい場合 {@code true}
     * @throws DomaNullPointerException
     *             比較対象のドメインが {@code null} の場合
     */
    boolean eq(D other);

    /**
     * このインスタンスの値が比較対象の値よりも大きいか等しい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象の値
     * @return 大きいか等しい場合 {@code true}
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象の値が {@code null} の場合
     */
    boolean ge(V other);

    /**
     * このインスタンスの値が比較対象のドメインの値と大きいか等しい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象のドメイン
     * @return 大きいか等しい場合 {@code true} @ 比較対象のドメインが {@code null} の場合
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象のドメイン値が {@code null} の場合
     */
    boolean ge(D other);

    /**
     * このインスタンスの値が比較対象の値よりも大きい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象の値
     * @return 大きい場合 {@code true}
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象の値が {@code null} の場合
     */
    boolean gt(V other);

    /**
     * このインスタンスの値が比較対象のドメインの値よりも大きい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象のドメイン
     * @return 大きい場合 {@code true} @ 比較対象のドメインが {@code null} の場合
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象のドメイン値が {@code null} の場合
     */
    boolean gt(D other);

    /**
     * このインスタンスの値が比較対象の値よりも小さいか等しい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象の値
     * @return 小さいか等しい場合 {@code true}
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象の値が {@code null} の場合
     */
    boolean le(V other);

    /**
     * このインスタンスの値が比較対象のドメインの値よりも小さいか等しい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象のドメイン
     * @return 小さいか等しい場合 {@code true} @ 比較対象のドメインが {@code null} の場合
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象のドメイン値が {@code null} の場合
     */
    boolean le(D other);

    /**
     * このインスタンスの値が比較対象の値よりも小さい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象の値
     * @return 小さい場合 {@code true}
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象の値が {@code null} の場合
     */
    boolean lt(V other);

    /**
     * このインスタンスの値が比較対象のドメインの値よりも小さい場合 {@code true} を返します。
     * 
     * @param other
     *            比較対象のドメイン
     * @return 小さいか等しい場合 {@code true} @ 比較対象のドメインが {@code null} の場合
     * @throws DomainIncomparableException
     *             このインスタンスの値もしくは、比較対象のドメイン値が {@code null} の場合
     */
    boolean lt(D other);

}
