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
package org.seasar.doma.jdbc;

import java.lang.reflect.Method;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.MapKeyNamingType;

/**
 * マップのキーのネーミング規約の適用を制御します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public interface MapKeyNaming {

    /**
     * マップのキーのネーミング規約を適用します。
     * 
     * @param method
     *            Daoのメソッド、クエリビルダを使った場合には {@code null}
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @param text
     *            規約が適用される文字列
     * @return 規約が適用された文字列
     * @throws DomaNullPointerException
     *             {@code mapKeyNamingType} もしくは {@code text} が {@code null} の場合
     */
    default String apply(Method method, MapKeyNamingType mapKeyNamingType, String text) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (text == null) {
            throw new DomaNullPointerException("text");
        }
        return mapKeyNamingType.apply(text);
    }
}
