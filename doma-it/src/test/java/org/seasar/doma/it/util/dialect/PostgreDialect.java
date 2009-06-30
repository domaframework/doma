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

/**
 * PostgreSQL用の方言です。
 * 
 * @author taedium
 */
public class PostgreDialect extends StandardDialect {

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new PostgreSqlBlockContext();
    }

    /**
     * PostgreSQL用の{@link SqlBlockContext}の実装クラスです。
     * 
     * @author taedium
     */
    public static class PostgreSqlBlockContext implements SqlBlockContext {

        /** ブロックの内側の場合{@code true} */
        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if ("$$".equals(keyword)) {
                inSqlBlock = !inSqlBlock;
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }
}
