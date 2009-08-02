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
package org.seasar.doma.copy;

import java.util.Map;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link CopyUtil} から処理を委譲されるクラスです。
 * <p>
 * このインタフェースの実装はスレッドセーフではければいけません。
 * </p>
 * 
 * @author taedium
 */
public interface CopyUtilDelegate {

    /**
     * オブジェクトをコピーします。
     * <p>
     * 次の形式のコピーを行います。
     * <ul>
     * <li>エンティティからエンティティへのコピー
     * <li>エンティティから {@literal JavaBeans} へのコピー
     * <li> {@literal JavaBeans} からエンティティへのコピー
     * <li> {@literal JavaBeans} から {@literal JavaBeans} へのコピー
     * </ul>
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     * @see CopyUtilDelegate#copy(Object, Object, CopyOptions)
     */
    void copy(Object src, Object dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

    /**
     * オプションを指定してオブジェクトを {@link Map} にコピーします。
     * <p>
     * 次の形式のコピーを行います。
     * <ul>
     * <li>エンティティから {@code Map} へのコピー
     * <li> {@literal JavaBeans} から {@code Map} へのコピー
     * </ul>
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     * @param copyOptions
     *            オプション
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     * @see CopyUtilDelegate#copy(Map, Object, CopyOptions)
     */
    void copy(Object src, Map<String, Object> dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

    /**
     * オプションを指定して{@link Map} をオブジェクトにコピーします。
     * <p>
     * 次の形式のコピーを行います。
     * <ul>
     * <li> {@code Map} からエンティティへのコピー
     * <li> {@code Map} から{@literal JavaBeans} へのコピー
     * </ul>
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     * @param copyOptions
     *            オプション
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     * @see CopyUtilDelegate#copy(Object, Map, CopyOptions)
     */
    void copy(Map<String, Object> src, Object dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

}
