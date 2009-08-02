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
package org.seasar.doma.entity;

/**
 * エンティティのリスナーです。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <E>
 *            エンティティの型
 */
public interface EntityListener<E> {

    /**
     * 挿入処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preInsert(E entity);

    /**
     * 更新処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preUpdate(E entity);

    /**
     * 削除処理の前処理を行います。
     * 
     * @param entity
     *            エンティティ
     */
    void preDelete(E entity);
}
