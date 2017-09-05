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

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate a JDBC related error.
 */
public class JdbcException extends DomaException {

    private static final long serialVersionUID = 1L;

    public JdbcException(MessageResource messageCode, Object... args) {
        super(messageCode, args);
    }

    public JdbcException(MessageResource messageCode, Throwable cause, Object... args) {
        super(messageCode, cause, args);
    }

    /**
     * Chooses the SQL string for logging depending on the log type
     * 
     * @param logType
     *            the log type
     * @param rawSql
     *            the raw SQL string
     * @param formattedSql
     *            the formatted SQL string
     * @return the SQL string
     */
    protected static String choiceSql(SqlLogType logType, String rawSql, String formattedSql) {
        switch (logType) {
        case RAW:
            return rawSql;
        case FORMATTED:
            return formattedSql;
        case NONE:
            return "";
        default:
            throw new AssertionError("unreachable");
        }
    }
}
