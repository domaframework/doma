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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.wrapper.Wrapper;

/**
 * プロパティのアクセサです。
 * 
 * @author nakamura-to
 * 
 * @param <E>
 *            エンティティの型
 * @param <V>
 *            値の型
 * @since 2.0.0
 */
public interface PropertyState<E, V> {

    /**
     * プロパティを返します。
     * 
     * @return プロパティ
     */
    Object get();

    /**
     * エンティティからこのアクセサへ値を読み込みます。
     * 
     * @param entity
     *            エンティティ
     * @return このアクセサ
     */
    PropertyState<E, V> load(E entity);

    /**
     * エンティティへこのアクセサが保持する値を保存します。
     * 
     * @param entity
     * @return このアクセサ
     */
    PropertyState<E, V> save(E entity);

    /**
     * 値のラッパーを返します。
     * 
     * @return 値のラッパー
     */
    Wrapper<V> getWrapper();

}
