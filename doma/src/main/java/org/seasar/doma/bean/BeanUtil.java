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
package org.seasar.doma.bean;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@literal JavaBeans} に関するユーティリティです。
 * 
 * <h4>振る舞いの変更</h4>
 * 
 * このクラスの振る舞いは、 {@link #setDelegate(BeanUtilDelegate)} に任意の実装を設定することより変更できます。
 * <p>
 * 
 * @author taedium
 * 
 */
public final class BeanUtil {

    private static volatile BeanUtilDelegate delegate = new BuiltinBeanUtilDelegate();

    /**
     * 委譲先を設定します。
     * 
     * @param delegate
     *            委譲先
     * @throws DomaNullPointerException
     *             {@code delegate} が {@code null} の場合
     */
    public static void setDelegate(BeanUtilDelegate delegate)
            throws DomaNullPointerException {
        if (delegate == null) {
            throw new DomaNullPointerException("delegate");
        }
        BeanUtil.delegate = delegate;
    }

    /**
     * {@literal JavaBeans} をラップします。
     * 
     * @param bean
     *            {@literal JavaBeans}
     * @return {@literal JavaBeans} のラッパー
     */
    public static BeanWrapper wrap(Object bean) {
        return delegate.wrap(bean);
    }
}
