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
package org.seasar.doma.internal.jdbc.sql.node;

/**
 * @author taedium
 * 
 */
public class SqlLocation {

    protected final String sql;

    protected final int lineNumber;

    protected final int position;

    public SqlLocation(String sql, int lineNumber, int position) {
        this.sql = sql;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    public String getSql() {
        return sql;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return sql + ":" + lineNumber + ":" + position;
    }
}
