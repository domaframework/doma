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

import org.seasar.doma.wrapper.Wrapper;

/**
 * SQLのバインド変数の値をSQLのログ用文字列に変換する処理を表します。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * 
 */
public interface SqlLogFormattingFunction {

    /**
     * この処理を適用します。
     * 
     * @param <V>
     *            値の型
     * @param wrapper
     *            SQLのバインド変数にマッピングされるラッパー
     * @param formatter
     *            ログ用のフォーマッタ
     * @return フォーマットされた文字列
     */
    <V> String apply(Wrapper<V> wrapper, SqlLogFormatter<V> formatter);
}
