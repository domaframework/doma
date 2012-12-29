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

/**
 * クラスのヘルパーです。
 * <p>
 * クラスの扱いに関してアプリケーションサーバやフレームワークの差異を抽象化します。
 * 
 * @author taedium
 * @since 1.27.0
 */
public interface ClassHelper {

    /**
     * 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、{@link Class} オブジェクトを返します。
     * 
     * @param className
     *            要求するクラスの完全指定の名前
     * @return 指定された名前を持つクラスの {@link Class} オブジェクト
     * @throws Exception
     *             例外
     * @see Class#forName(String)
     */
    <T> Class<T> forName(String className) throws Exception;
}
