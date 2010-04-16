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

import org.seasar.doma.internal.message.Message;

/**
 * バッチ処理に失敗した場合にスローされる例外です。
 * <p>
 * {@link #getFormattedSql()}は {@code null} を返します。
 * <p>
 * 
 * @author taedium
 * 
 */
public class BatchSqlExecutionException extends SqlExecutionException {

    private static final long serialVersionUID = 1L;

    /**
     * SQL、スローされた原因、根本原因を指定してインスタンスを構築します。
     * 
     * @param sql
     *            SQL
     * @param cause
     *            スローされた原因
     * @param rootCause
     *            根本原因
     */
    public BatchSqlExecutionException(Sql<?> sql, Throwable cause,
            Throwable rootCause) {
        this(sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause,
                rootCause);
    }

    /**
     * 未加工SQL、スローされた原因、根本原因を指定してインスタンスを構築します。
     * 
     * @param kind
     *            SQLの種別
     * @param rawSql
     *            未加工SQL
     * @param sqlFilePath
     *            SQLファイルのパス
     * @param cause
     *            スローされた原因
     * @param rootCause
     *            根本原因
     */
    public BatchSqlExecutionException(SqlKind kind, String rawSql,
            String sqlFilePath, Throwable cause, Throwable rootCause) {
        super(Message.DOMA2030, kind, rawSql, null, sqlFilePath, cause,
                rootCause);
    }

}
