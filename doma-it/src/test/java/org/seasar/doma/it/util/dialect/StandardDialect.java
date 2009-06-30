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
package org.seasar.doma.it.util.dialect;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.it.util.Dialect;

/**
 * 標準の方言です。
 * 
 * @author taedium
 */
public class StandardDialect implements Dialect {

    public SqlBlockContext createSqlBlockContext() {
        return new StandardSqlBlockContext();
    }

    public String getSqlBlockDelimiter() {
        return null;
    }

    /**
     * 標準の{@link SqlBlockContext}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardSqlBlockContext implements SqlBlockContext {

        /** SQLブロックの開始を表すキーワードの連なりのリスト */
        protected List<List<String>> sqlBlockStartKeywordsList =
            new ArrayList<List<String>>();

        /** 追加されたキーワードの連なり */
        protected List<String> keywords = new ArrayList<String>();

        /** SQLブロックの内側の場合{@code true} */
        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if (!inSqlBlock) {
                keywords.add(keyword);
                check();
            }
        }

        /**
         * ブロックの内側かどうかチェックします。
         */
        protected void check() {
            for (List<String> startKeywords : sqlBlockStartKeywordsList) {
                if (startKeywords.size() > keywords.size()) {
                    continue;
                }
                for (int i = 0; i < startKeywords.size(); i++) {
                    String word1 = startKeywords.get(i);
                    String word2 = keywords.get(i);
                    inSqlBlock = word1.equalsIgnoreCase(word2);
                    if (!inSqlBlock) {
                        break;
                    }
                }
                if (inSqlBlock) {
                    break;
                }
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }

}
