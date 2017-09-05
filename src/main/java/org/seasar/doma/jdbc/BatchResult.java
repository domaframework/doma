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

import java.util.Arrays;
import java.util.List;

/**
 * A batch processing result for immutable entities.
 * 
 * @param <ENTITY>
 *            the entity type
 */
public class BatchResult<ENTITY> {

    private final int[] counts;

    private final List<ENTITY> entities;

    /**
     * Creates an instance.
     * 
     * @param counts
     *            the array of the affected row count
     * @param entities
     *            the entity list
     */
    public BatchResult(int[] counts, List<ENTITY> entities) {
        this.counts = counts;
        this.entities = entities;
    }

    /**
     * Returns the array of the affected row count.
     * 
     * @return the array of the affected row count
     */
    public int[] getCounts() {
        return counts;
    }

    /**
     * Returns the entity list.
     * 
     * @return the entity list
     */
    public List<ENTITY> getEntities() {
        return entities;
    }

    /**
     * Returns the entity list.
     * 
     * @return the entity list
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public List<ENTITY> component1() {
        return entities;
    }

    /**
     * Returns the array of the affected row count.
     * 
     * @return the array of the affected row count
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public int[] component2() {
        return counts;
    }

    @Override
    public String toString() {
        return "BatchResult(entities=" + entities + ", counts=" + Arrays.toString(counts) + ")";
    }

}
