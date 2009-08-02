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
import org.seasar.doma.bean.BeanUtil;
import org.seasar.doma.bean.BeanUtilDelegate;
import org.seasar.doma.entity.Entity;

/**
 * コピーに関するユーティリティです。
 * 
 * <h4>サポートする形式</h4>
 * 
 * 次の形式のコピーをサポートしています。
 * <ul>
 * <li>エンティティからエンティティへのコピー
 * <li>エンティティから {@literal JavaBeans} へのコピー
 * <li>エンティティから {@link Map} へのコピー
 * <li> {@literal JavaBeans} からエンティティへのコピー
 * <li> {@literal JavaBeans} から {@literal JavaBeans} へのコピー
 * <li> {@literal JavaBeans} から {@code Map} へのコピー
 * <li> {@code Map} から エンティティへのコピー
 * <li> {@code Map} から {@literal JavaBeans} へのコピー
 * </ul>
 * <p>
 * 
 * エンティティであるかどうかは、 {@code Object} が {@link Entity} のサブタイプであるかどうかで判断されます。
 * エンティティでない {@code Object} は、 {@literal JavaBeans} とみなされます。{@code Map}
 * はパラメータの型により自ずから {@code Map} と判定されます。
 * 
 * <h4>コピーの仕様</h4>
 * 
 * 同じ名前のプロパティをコピーします。 {@code Map} は、キーがプロパティ名とみなされます。
 * 
 * <h4>{@literal JavaBeans}へのアクセス方法</h4>
 * {@literal JavaBeans} へのアクセス方法は、変更可能です。次の2つの手段があります。
 * <ul>
 * <li>
 * {@code copy} メソッドの呼び出しだけに閉じた局所的な方法。
 * {@link CopyOptions#beanWrapperFactory(org.seasar.doma.bean.BeanWrapperFactory)}
 * に任意の実装を設定し、 {@code CopyOptions} オブジェクトをパラメータに {@code copy} を呼び出します。
 * <li>全スレッドで共有するグローバルな変更方法。 {@link BeanUtilDelegate#wrap(Object)}
 * を実装し、そのインスタンスを
 * {@link BeanUtil#setDelegate(org.seasar.doma.bean.BeanUtilDelegate)}に設定します。
 * </ul>
 * 
 * <h4>振る舞いの変更</h4>
 * このクラスの振る舞いは、 {@link #setDelegate(CopyUtilDelegate)} に任意の実装を設定することより変更できます。
 * <p>
 * 
 * @author taedium
 * 
 */
public final class CopyUtil {

    private static volatile CopyUtilDelegate delegate = new BuiltinCopyUtilDelegate();

    /**
     * 委譲先を設定します。
     * 
     * @param delegate
     *            委譲先
     */
    public static void setDelegate(CopyUtilDelegate delegate)
            throws DomaNullPointerException {
        if (delegate == null) {
            throw new DomaNullPointerException("delegate");
        }
        CopyUtil.delegate = delegate;
    }

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
     */
    public static void copy(Object src, Object dest)
            throws DomaNullPointerException, CopyException {
        delegate.copy(src, dest, new CopyOptions());
    }

    /**
     * オプションを指定してオブジェクトをコピーします。
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
     * @param copyOptions
     *            オプション
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     */
    public static void copy(Object src, Object dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException {
        delegate.copy(src, dest, copyOptions);
    }

    /**
     * オブジェクトを {@link Map} にコピーします。
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
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     */
    public static void copy(Object src, Map<String, Object> dest)
            throws DomaNullPointerException, CopyException {
        delegate.copy(src, dest, new CopyOptions());
    }

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
     */
    public static void copy(Object src, Map<String, Object> dest,
            CopyOptions copyOptions) throws DomaNullPointerException,
            CopyException {
        delegate.copy(src, dest, copyOptions);
    }

    /**
     * {@link Map} をオブジェクトにコピーします。
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
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws CopyException
     *             コピーに失敗した場合
     */
    public static void copy(Map<String, Object> src, Object dest)
            throws DomaNullPointerException, CopyException {
        delegate.copy(src, dest, new CopyOptions());
    }

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
     */
    public static void copy(Map<String, Object> src, Object dest,
            CopyOptions copyOptions) throws DomaNullPointerException,
            CopyException {
        delegate.copy(src, dest, copyOptions);
    }
}
