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
 * {@link #iterate(Object, IterationContext)} の実行後に任意の処理を行うコールバックです。
 * <p>
 * {@code iterate} の実行結果に変更を加えることも可能です。
 * 
 * @author taedium
 * @since 1.21.0
 */
public interface PostIterationCallback<R, T> extends IterationCallback<R, T> {

    /**
     * {@code iterate} の実行後に呼び出され、任意の処理を実行します。
     * 
     * @param result
     *            {@code iterate} の実行結果
     * @param context
     *            実行コンテキスト
     * @return 実行結果
     */
    R postIterate(R result, IterationContext context);
}
