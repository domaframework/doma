/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.it.helper.dialect;


/**
 * @author taedium
 * 
 */
public interface ToolDialect {

    /**
     * SQLブロックを生成します。
     * 
     * @return SQLブロック
     */
    SqlBlockContext createSqlBlockContext();

    /**
     * SQLブロックの区切り文字を返します。
     * 
     * @return SQLブロックの区切り文字
     */
    String getSqlBlockDelimiter();

    /**
     * SQLブロックのコンテキストです。
     * <p>
     * SQLの解析中に、SQLがステートメントではなくブロック（ステートメントの集合）として扱われているかどうかを判断するために使用されます。
     * <p>
     * 
     * @author taedium
     * 
     */
    public static interface SqlBlockContext {

        /**
         * SQLのキーワードを追加します。
         * 
         * @param keyword
         *            SQLのキーワード
         */
        void addKeyword(String keyword);

        /**
         * SQLブロックを処理しているかどうかを返します。
         * 
         * @return SQLブロックを処理している場合 {@code true}
         */
        boolean isInSqlBlock();
    }
}
