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
 * スクリプト内のSQLブロックのコンテキストです。
 * <p>
 * スクリプトの解析中に、SQLがステートメントではなくブロック（ステートメントの集合）として扱われているかどうかを判断するために使用されます。
 * <p>
 * 
 * @author taedium
 * @since 1.7.0
 */
public interface ScriptBlockContext {

    /**
     * SQLのキーワードを追加します。
     * 
     * @param keyword
     *            SQLのキーワード
     */
    void addKeyword(String keyword);

    /**
     * ブロックを処理しているかどうかを返します。
     * 
     * @return ブロックを処理している場合 {@code true}
     */
    boolean isInBlock();
}
