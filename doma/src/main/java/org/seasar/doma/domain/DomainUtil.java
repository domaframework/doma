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

/**
 * {@link Domain} のユーティリティクラスです。
 * <p>
 * {@link #setDelegate(DomainUtilDelegate)} により振る舞いを変更できます。
 * 
 * @author taedium
 * 
 */
public final class DomainUtil {

    private static volatile DomainUtilDelegate delegate = new BuiltinDomainUtilDelegate();

    /**
     * 委譲先を設定します。
     * 
     * @param delegate
     *            委譲先
     */
    public static void setDelegate(DomainUtilDelegate delegate) {
        DomainUtil.delegate = delegate;
    }

    /**
     * リフレクションを使って値を設定します。
     * 
     * @param domain
     *            ドメイン
     * @param value
     *            値
     * @throws DomainReflectionException
     *             値の設定に失敗した場合
     */
    public static void set(Domain<?, ?> domain, Object value)
            throws DomainReflectionException {
        delegate.set(domain, value);
    }
}
