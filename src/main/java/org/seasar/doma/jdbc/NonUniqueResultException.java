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

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the number of fetched rows is expected less than or
 * equal to 1, but actually it is greater than or equal to 2.
 */
public class NonUniqueResultException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final SqlKind kind;

    protected final String rawSql;

    protected final String formattedSql;

    protected final String sqlFilePath;

    public NonUniqueResultException(SqlLogType logType, Sql<?> sql) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getFormattedSql(), sql.getSqlFilePath());
    }

    public NonUniqueResultException(SqlLogType logType, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath) {
        super(Message.DOMA2001, sqlFilePath, choiceSql(logType, rawSql, formattedSql));
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
    }

    /**
     * Returns the SQL kind.
     * 
     * @return the SQL kind
     */
    public SqlKind getKind() {
        return kind;
    }

    /**
     * Returns the raw SQL string.
     * 
     * @return the raw SQL string
     */
    public String getRawSql() {
        return rawSql;
    }

    /**
     * Returns the formatted SQL string.
     * 
     * @return the formatted SQL string
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * Returns the SQL file path
     * 
     * @return the SQL file path or {@code null} if the SQL is auto generated
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

}
