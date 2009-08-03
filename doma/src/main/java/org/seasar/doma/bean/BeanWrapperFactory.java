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
 * {@link BeanWrapper} のファクトリです。
 * <p>
 * このインタフェースの実装はスレッドセーフではければいけません。
 * </p>
 * 
 * @author taedium
 * 
 */
public interface BeanWrapperFactory {

    /**
     * {@link BeanWrapper} の実装を作成します。
     * 
     * @param bean
     *            {@literal JavaBeans}
     * @return {@link BeanWrapper} の実装
     * @throws DomaNullPointerException
     *             {@code bean} が {@code null} の場合
     */
    BeanWrapper create(Object bean);
}
