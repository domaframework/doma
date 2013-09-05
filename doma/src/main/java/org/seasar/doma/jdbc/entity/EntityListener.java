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

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;

/**
 * エンティティのリスナーです。
 * <p>
 * このインタフェースの実装は、引数なしの {@code public} なコンストラクタを持たなければいけません。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * 
 * @param <E>
 *            エンティティの型
 */
public interface EntityListener<E> {

    /**
     * 挿入処理の前処理を行います。
     * <p>
     * 対象となるのは、{@link Insert} 、または {@link BatchInsert}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void preInsert(E entity, PreInsertContext<E> context);

    /**
     * 更新処理の前処理を行います。
     * <p>
     * 対象となるのは、{@link Update} 、または {@link BatchUpdate}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void preUpdate(E entity, PreUpdateContext<E> context);

    /**
     * 削除処理の前処理を行います。
     * <p>
     * 対象となるのは、{@link Delete} 、または {@link BatchDelete}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void preDelete(E entity, PreDeleteContext<E> context);

    /**
     * 挿入処理の後処理を行います。
     * <p>
     * 対象となるのは、{@link Insert} 、または {@link BatchInsert}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void postInsert(E entity, PostInsertContext<E> context);

    /**
     * 更新処理の後処理を行います。
     * <p>
     * 対象となるのは、{@link Update} 、または {@link BatchUpdate}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void postUpdate(E entity, PostUpdateContext<E> context);

    /**
     * 削除処理の後処理を行います。
     * <p>
     * 対象となるのは、{@link Delete} 、または {@link BatchDelete}
     * のパラメータにエンティティを受け取るDaoメソッドの実行です。
     * 
     * @param entity
     *            エンティティ
     * @param context
     *            コンテキスト
     * @since 1.11.0
     */
    void postDelete(E entity, PostDeleteContext<E> context);
}
