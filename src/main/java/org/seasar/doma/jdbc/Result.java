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
 * イミュータブルなエンティティに対する更新や挿入の結果を表します。
 * 
 * @author taedium
 * @param <ENTITY>
 *            エンティティ
 * @since 1.34.0
 */
public class Result<ENTITY> {

    private final int count;

    private final ENTITY entity;

    /**
     * インスタンスを構築します。
     * 
     * @param count
     *            更新件数
     * @param entity
     *            エンティティ
     */
    public Result(int count, ENTITY entity) {
        this.count = count;
        this.entity = entity;
    }

    /**
     * 更新件数を返します。
     * 
     * @return 更新件数
     */
    public int getCount() {
        return count;
    }

    /**
     * エンティティを返します。
     * 
     * @return エンティティ
     */
    public ENTITY getEntity() {
        return entity;
    }

    /**
     * エンティティを返します。
     * 
     * @return エンティティ
     * @see <a
     *      href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public ENTITY component1() {
        return entity;
    }

    /**
     * 更新件数を返します。
     * 
     * @return 更新件数
     * @see <a
     *      href="https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public int component2() {
        return count;
    }

    @Override
    public String toString() {
        return "Result(entity=" + entity + ", count=" + count + ")";
    }

}
