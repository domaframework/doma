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
 * イミュータブルなエンティティに対するバッチ更新やバッチ挿入の結果を表します。
 * 
 * @author taedium
 * @since 1.34.0
 */
public class BatchResult<E> {

    private final int[] counts;

    private final List<E> entities;

    /**
     * 
     * @param counts
     *            更新件数の配列
     * @param entities
     *            エンティティのリスト
     */
    public BatchResult(int[] counts, List<E> entities) {
        this.counts = counts;
        this.entities = entities;
    }

    /**
     * 更新件数の配列を返します。
     * 
     * 
     * @return 更新件数の配列
     */
    public int[] getCounts() {
        return counts;
    }

    /**
     * エンティティのリストを返します。
     * 
     * @return エンティティのリスト
     */
    public List<E> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return "BatchResult [counts=" + Arrays.toString(counts) + ", entities="
                + entities + "]";
    }

}
