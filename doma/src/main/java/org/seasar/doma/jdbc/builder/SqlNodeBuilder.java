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
package org.seasar.doma.jdbc.builder;

import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
class SqlNodeBuilder {

    private static final String lineSeparator = System
            .getProperty("line.separator");

    private final StringBuilder buf = new StringBuilder(200);

    SqlNodeBuilder() {
    }

    void appendSql(String fragment) {
        buf.append(fragment);
    }

    void appendSqlWithLineSeparator(String fragment) {
        if (buf.length() > 0) {
            buf.append(lineSeparator);
        }
        buf.append(fragment);
    }

    void cutBackSql(int length) {
        if (buf.length() > length) {
            buf.setLength(buf.length() - length);
        }
    }

    void appendParameter(String paramName) {
        buf.append("/* " + paramName + " */0");
    }

    public SqlNode build() {
        SqlParser parser = new SqlParser(buf.toString());
        return parser.parse();
    }
}
