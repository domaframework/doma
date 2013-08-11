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

import java.sql.ResultSet;

/**
 * {@link ResultSet} にマッピングされるオブジェクト群を1件ずつ処理するコールバックです。
 * <p>
 * {@link #iterate(Object, IterationContext)} は、 {@code ResultSet}
 * のループ中にオブジェクトがインスタンス化された直後に呼び出されます。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * 
 * @param <R>
 *            戻り値の型
 * @param <T>
 *            処理対象の型。すなわち、基本型、ドメインクラス、エンティティクラス、もしくは {@code Map<String, Object>}
 */
public interface IterationCallback<R, T> {

    /**
     * 処理対象のオブジェクト群を順に1件ずつ処理します。
     * <p>
     * 全件を処理する前に処理を中断するには、 {@link IterationContext#exit()}を呼び出します。
     * 
     * @param target
     *            {@link ResultSet} の1行にマッピングされたオブジェクト
     * @param context
     *            実行コンテキスト
     * @return 任意の実行結果
     */
    R iterate(T target, IterationContext context);
}
