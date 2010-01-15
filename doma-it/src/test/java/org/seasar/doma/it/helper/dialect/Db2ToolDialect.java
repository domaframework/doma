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

import java.util.Arrays;

/**
 * @author taedium
 * 
 */
public class Db2ToolDialect extends StandardToolDialect {

    @Override
    public String getSqlBlockDelimiter() {
        return "@";
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new Db2SqlBlockContext();
    }

    public static class Db2SqlBlockContext extends StandardSqlBlockContext {

        protected Db2SqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
        }
    }
}
