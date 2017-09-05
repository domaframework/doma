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
 * A processing result for an immutable entity.
 * 
 * @param <ENTITY>
 *            the entity type
 */
public class Result<ENTITY> {

    private final int count;

    private final ENTITY entity;

    /**
     * Creates an instance.
     * 
     * @param count
     *            the affected row count
     * @param entity
     *            the entity
     */
    public Result(int count, ENTITY entity) {
        this.count = count;
        this.entity = entity;
    }

    /**
     * Returns the affected row count.
     * 
     * @return the affected row count
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns the entity.
     * 
     * @return the entity
     */
    public ENTITY getEntity() {
        return entity;
    }

    /**
     * Returns the entity.
     * 
     * @return the entity
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public ENTITY component1() {
        return entity;
    }

    /**
     * Returns the affected row count.
     * 
     * @return the affected row count
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
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
