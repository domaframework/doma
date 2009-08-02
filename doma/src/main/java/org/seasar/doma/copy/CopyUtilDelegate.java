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
 * メソッドの仕様は {@link CopyUtil} に従います。
 * <p>
 * このインタフェースの実装はスレッドセーフではければいけません。
 * </p>
 * 
 * @author taedium
 */
public interface CopyUtilDelegate {

    /**
     * @param src
     * @param dest
     * @param copyOptions
     * @throws DomaNullPointerException
     * @throws CopyException
     * @see CopyUtil#copy(Object, Object)
     * @see CopyUtil#copy(Object, Object, CopyOptions)
     */
    void copy(Object src, Object dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

    /**
     * @param src
     * @param dest
     * @param copyOptions
     * @throws DomaNullPointerException
     * @throws CopyException
     * @see CopyUtil#copy(Object, Map)
     * @see CopyUtil#copy(Object, Map, CopyOptions)
     */
    void copy(Object src, Map<String, Object> dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

    /**
     * @param src
     * @param dest
     * @param copyOptions
     * @throws DomaNullPointerException
     * @throws CopyException
     * @see CopyUtil#copy(Map, Object)
     * @see CopyUtil#copy(Map, Object, CopyOptions)
     */
    void copy(Map<String, Object> src, Object dest, CopyOptions copyOptions)
            throws DomaNullPointerException, CopyException;

}
