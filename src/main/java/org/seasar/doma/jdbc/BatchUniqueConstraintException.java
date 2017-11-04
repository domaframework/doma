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
 * Thrown to indicate that an unique constraint violation occurs in a batch
 * processing.
 * <p>
 * {@link #getFormattedSql()} returns {@code null}.
 */
public class BatchUniqueConstraintException extends UniqueConstraintException {

    private static final long serialVersionUID = 1L;

    public BatchUniqueConstraintException(SqlLogType logType, Sql<?> sql, Throwable cause) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause);
    }

    public BatchUniqueConstraintException(SqlLogType logType, SqlKind kind, String rawSql,
            String sqlFilePath, Throwable cause) {
        super(Message.DOMA2029, kind, choiceSql(logType, rawSql, rawSql), sqlFilePath, cause);
    }

}
