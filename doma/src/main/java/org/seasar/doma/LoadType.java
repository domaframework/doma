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
package org.seasar.doma;

import java.util.List;
import java.util.stream.Stream;

import org.seasar.doma.jdbc.IterationCallback;

/**
 * 検索結果をエンティティなどのマッピング対象オブジェクトへロードする方法です。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public enum LoadType {

    /**
     * 一括でロードします。結果が複数件ある場合は {@link List} に蓄積します。
     */
    BULK,

    /**
     * {@link IterationCallback} を使って1件ずつ反復的にロードします。
     */
    ITERATION,

    /**
     * {@link Stream} を使ってロードします。
     */
    STREAM
}
